import pandas as pd
from sqlalchemy import create_engine 
from sqlalchemy.orm import Session
from sqlalchemy import select

from app.domain.Product import Product

from app.common.config import core_product_connection_str

engine = create_engine(core_product_connection_str)

def get_products_as_DF():
    cnx = engine.connect()
    df = pd.read_sql_table('product', cnx, columns=['id', 'name', 'description']) 
    
    return df

def get_all_products():
    with Session(engine) as session:
        query = select(Product)
        products = session.scalars(query).all()
        
    return list(products)

def get_product_by_id(id):
    with Session(engine) as session:
        query = select(Product).where(Product.id == id)
        product = session.scalars(query).first()
        
    return product