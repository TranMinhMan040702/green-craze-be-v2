from sqlalchemy.orm import Mapped
from sqlalchemy.orm import mapped_column
from app.domain.Base import Base

class Unit(Base):
    __tablename__ = "unit"
    
    id: Mapped[int] = mapped_column(primary_key=True)
    name: Mapped[str] = mapped_column()
    status: Mapped[bool] = mapped_column()