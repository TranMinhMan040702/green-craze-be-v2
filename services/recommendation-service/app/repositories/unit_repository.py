import pandas as pd
from sqlalchemy import create_engine 
from sqlalchemy.orm import Session
from sqlalchemy import select

from app.domain.Unit import Unit

from app.common.config import core_product_connection_str

engine = create_engine(core_product_connection_str)

def get_unit_by_id(id):
    with Session(engine) as session:
        query = select(Unit).where(Unit.id == id)
        unit = session.scalars(query).first()
        
    return unit