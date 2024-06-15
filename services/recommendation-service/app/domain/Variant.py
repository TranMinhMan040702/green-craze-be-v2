from sqlalchemy.orm import Mapped
from sqlalchemy.orm import mapped_column
from app.domain.Base import Base

class Variant(Base):
    __tablename__ = "variant"
    
    id: Mapped[int] = mapped_column(primary_key=True)
    name: Mapped[str] = mapped_column()
    sku: Mapped[str] = mapped_column()
    quantity: Mapped[int] = mapped_column()
    item_price: Mapped[float] = mapped_column()
    total_price: Mapped[float] = mapped_column()
    promotional_item_price: Mapped[float] = mapped_column()
    total_promotional_price: Mapped[float] = mapped_column()
    status: Mapped[str] = mapped_column()
    product_id: Mapped[int] = mapped_column()
    status: Mapped[bool] = mapped_column()