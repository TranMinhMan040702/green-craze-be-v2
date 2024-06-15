from sqlalchemy.orm import Mapped
from sqlalchemy.orm import mapped_column
from sqlalchemy.orm import relationship
from app.domain.Base import Base

class Product(Base):
    __tablename__ = "product"
    
    id: Mapped[int] = mapped_column(primary_key=True)
    name: Mapped[str] = mapped_column()
    short_description: Mapped[str] = mapped_column()
    description: Mapped[str] = mapped_column()
    code: Mapped[str] = mapped_column()
    quantity: Mapped[int] = mapped_column()
    actual_inventory: Mapped[int] = mapped_column()
    sold: Mapped[int] = mapped_column()
    rating: Mapped[float] = mapped_column()
    slug: Mapped[str] = mapped_column()
    cost: Mapped[float] = mapped_column()
    status: Mapped[str] = mapped_column()
    unit_id: Mapped[int] = mapped_column()
    product_category_id: Mapped[int] = mapped_column()
    brand_id: Mapped[int] = mapped_column()
