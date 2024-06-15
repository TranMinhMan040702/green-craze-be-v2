import pandas as pd
from sqlalchemy import create_engine 
from sqlalchemy.orm import Session
from sqlalchemy import select

from app.domain.ProductImage import ProductImage

from app.common.config import core_product_connection_str

engine = create_engine(core_product_connection_str)

def get_product_image_by_id(product_id):
    with Session(engine) as session:
        query = select(ProductImage).where(ProductImage.product_id == product_id)
        product_images = session.scalars(query).all()
        
    return product_images