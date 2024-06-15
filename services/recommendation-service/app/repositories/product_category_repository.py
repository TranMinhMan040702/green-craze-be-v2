import pandas as pd
from sqlalchemy import create_engine 
from sqlalchemy.orm import Session
from sqlalchemy import select

from app.domain.ProductCategory import ProductCategory

from app.common.config import core_product_connection_str

engine = create_engine(core_product_connection_str)

def get_product_category_by_id(id):
    with Session(engine) as session:
        query = select(ProductCategory).where(ProductCategory.id == id)
        product_category = session.scalars(query).first()
        
    return product_category