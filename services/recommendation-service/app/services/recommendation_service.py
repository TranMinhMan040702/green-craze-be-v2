import app.repositories.review_repository as review_repository
import app.repositories.product_category_repository as product_category_repository
import app.repositories.product_repository as product_repository
import app.repositories.product_images_repository as product_images_repository
import app.repositories.brand_repository as brand_repository
import app.repositories.unit_repository as unit_repository

from app.models.response.ProductResponse import ProductResponse

from sklearn.metrics.pairwise import cosine_similarity
from scipy.sparse.linalg import svds # for sparse matrices
import pandas as pd
import numpy as np
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
from scipy.sparse import csr_matrix

def create_final_ratings_matrix(df):
    final_ratings_matrix = df.pivot_table(index = 'user_id', columns ='product_id', values = 'rating', aggfunc='first').fillna(0)
    useridIndices = final_ratings_matrix.index.to_numpy()
    listIndex = []
    for id in useridIndices:
        valArr = df[df['user_id'] == id]['id'].to_numpy()
        listIndex.append(valArr[0])
    final_ratings_matrix['user_index'] = listIndex
    final_ratings_matrix.set_index(['user_index'], inplace=True)
    
    return final_ratings_matrix

# defining a function to get similar users
def similar_users(user_index, interactions_matrix):
    similarity = []
    for user in range(1, interactions_matrix.shape[0] + 1): #  .shape[0] gives number of rows
        
        #finding cosine similarity between the user_id and each user
        sim = cosine_similarity([interactions_matrix.loc[user_index]], [interactions_matrix.loc[user]])
        
        #Appending the user and the corresponding similarity score with user_id as a tuple
        similarity.append((user,sim))
        
    similarity.sort(key=lambda x: x[1], reverse=True)
    most_similar_users = [tup[0] for tup in similarity] #Extract the user from each tuple in the sorted list
    similarity_score = [tup[1] for tup in similarity] ##Extracting the similarity score from each tuple in the sorted list
   
    #Remove the original user and its similarity score and keep only other similar users 
    most_similar_users.remove(user_index)
    similarity_score.remove(similarity_score[0])
       
    return most_similar_users, similarity_score

# defining the recommendations function to get recommendations by using the similar users' preferences
def recommendations(user_index, num_of_products, interactions_matrix):
    #Saving similar users using the function similar_users defined above
    most_similar_users = similar_users(user_index, interactions_matrix)[0]
    
    #Finding product IDs with which the user_id has interacted
    prod_ids = set(list(interactions_matrix.columns[np.where(interactions_matrix.loc[user_index] > 0)]))
    recommendations = []
    
    observed_interactions = prod_ids.copy()
    for similar_user in most_similar_users:
        if len(recommendations) < num_of_products:
            
            #Finding 'n' products which have been rated by similar users but not by the user_id
            similar_user_prod_ids = set(list(interactions_matrix.columns[np.where(interactions_matrix.loc[similar_user] > 0)]))
            recommendations.extend(list(similar_user_prod_ids.difference(observed_interactions)))
            observed_interactions = observed_interactions.union(similar_user_prod_ids)
        else:
            break
    
    return recommendations[:num_of_products]

def recommendations_by_model_based(user_index, interactions_matrix, preds_matrix, num_recommendations):
    # Get the user's ratings from the actual and predicted interaction matrices
    user_ratings = interactions_matrix[user_index,:].toarray().reshape(-1)
    user_predictions = preds_matrix[user_index,:].toarray().reshape(-1)

    #Creating a dataframe with actual and predicted ratings columns
    temp = pd.DataFrame({'user_ratings': user_ratings, 'user_predictions': user_predictions})
    temp['Recommended Products'] = np.arange(len(user_ratings))
    temp = temp.set_index('Recommended Products')
    
    #Filtering the dataframe where actual ratings are 0 which implies that the user has not interacted with that product
    temp = temp.loc[temp.user_ratings == 0]   
    
    #Recommending products with top predicted ratings
    temp = temp.sort_values('user_predictions',ascending=False)#Sort the dataframe by user_predictions in descending order
    
    return temp['user_predictions'].head(num_recommendations).index.values

def get_recommendations_collaborative(df, user_id, num_product):
    final_ratings_matrix = create_final_ratings_matrix(df)
    user_array = df[df['user_id'] == user_id].to_numpy()
    
    if len(user_array) == 0:
        return []
    
    user_index = user_array[0][3]
    
    return recommendations(user_index, num_product, final_ratings_matrix)

def get_recommendations_collaborative_model_based(df, user_id, num_product):
    final_ratings_matrix = create_final_ratings_matrix(df)
    final_ratings_sparse = csr_matrix(final_ratings_matrix.values)
    U, s, Vt = svds(final_ratings_sparse, k = 10) # here k is the number of latent features
    # Construct diagonal array in SVD
    sigma = np.diag(s)
    all_user_predicted_ratings = np.dot(np.dot(U, sigma), Vt) 

    # Predicted ratings
    preds_df = pd.DataFrame(abs(all_user_predicted_ratings), columns = final_ratings_matrix.columns)
    preds_df.head()
    preds_matrix = csr_matrix(preds_df.values)
    
    user_array = df[df['user_id'] == user_id].to_numpy()
    user_index = user_array[0][3]
    
    product_indices = recommendations_by_model_based(user_index, final_ratings_sparse, preds_matrix, num_product)
    
    product_ids = []
    for idx in product_indices:
        product_ids.append(preds_df.columns[idx])
    
    return product_ids

def get_recommendations_content_based(df, product_name, num_recommend = 10):
    df['description'] = df['description'].fillna('')
    # Create a TfidfVectorizer and Remove stopwords
    tfidf = TfidfVectorizer(stop_words='english')
    
    # Fit and transform the data to a tfidf matrix
    tfidf_matrix = tfidf.fit_transform(df['description'])
    
    # Compute the cosine similarity between each movie description
    cosine_sim = linear_kernel(tfidf_matrix, tfidf_matrix)
    
    indices = pd.Series(df.index, index=df['name']).drop_duplicates()
    
    idx = indices[product_name]
    
    sim_scores = list(enumerate(cosine_sim[idx]))
    
    sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
    
    top_similar = sim_scores[1:num_recommend+1]
    
    product_indices = [i[0] for i in top_similar]
    
    return df['id'].iloc[product_indices].to_numpy()

def get_product_recommendations(productIdList):
    productList =[]
    for product_id in productIdList:
        product = product_repository.get_product_by_id(product_id)
        images = product_images_repository.get_product_image_by_id(product_id)
        brand = brand_repository.get_brand_by_id(product.brand_id)
        category = product_category_repository.get_product_category_by_id(product.product_category_id)
        unit = unit_repository.get_unit_by_id(product.unit_id)
        
        productResp = ProductResponse(product, brand, category, unit, images)
        productList.append(productResp.__dict__)
        
    return productList

def get_recommendations_by_user(user_id):
    try:
        ratings = review_repository.get_reviews_as_DF()
        results = get_recommendations_collaborative(ratings, user_id, 10)
        
        response = get_product_recommendations(results)
        print(response)
        
        return response
    except Exception as e:
        print(e)
        return []

def get_recommendations_by_product(product_id):
    try:
        products = product_repository.get_products_as_DF()
        product = product_repository.get_product_by_id(product_id)
        if product is None:
            return None
        
        results = get_recommendations_content_based(products, product.name, 5)
        
        response = get_product_recommendations(results)
        print(response)
        
        return response
    except Exception as e:
        print(e)
        return []
