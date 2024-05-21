import pandas as pd
from sqlalchemy import create_engine 
from sqlalchemy.orm import Session
from sqlalchemy import select

from app.domain.Brand import Brand

from app.common.config import core_product_connection_str

engine = create_engine(core_product_connection_str)

def get_brand_by_id(id):
    with Session(engine) as session:
        query = select(Brand).where(Brand.id == id)
        brand = session.scalars(query).first()
        
    return brand