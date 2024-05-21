from sqlalchemy.orm import Mapped
from sqlalchemy.orm import mapped_column
from app.domain.Base import Base

class ProductCategory(Base):
    __tablename__ = "product_category"
    
    id: Mapped[int] = mapped_column(primary_key=True)
    name: Mapped[str] = mapped_column()
    parent_id: Mapped[int] = mapped_column()
    slug: Mapped[str] = mapped_column()
    image: Mapped[str] = mapped_column()
    status: Mapped[bool] = mapped_column()