import pandas as pd
from sqlalchemy import create_engine 
from sqlalchemy.orm import Session
from sqlalchemy import select

from app.domain.Variant import Variant

from app.common.config import core_product_connection_str

engine = create_engine(core_product_connection_str)

def get_variant_by_product_id(product_id):
    with Session(engine) as session:
        query = select(Variant).where(Variant.product_id == product_id)
        variants = session.scalars(query).all()
        
    return variants