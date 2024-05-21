import pandas as pd
from sqlalchemy import create_engine 

from app.common.config import core_user_connection_str

def get_reviews_as_DF():
    cnx = create_engine(core_user_connection_str).connect()
    df = pd.read_sql_table('review', cnx, columns=['user_id', 'product_id', 'rating', 'id']) 
    return df