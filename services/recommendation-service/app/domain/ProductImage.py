from sqlalchemy.orm import Mapped
from sqlalchemy.orm import mapped_column
from app.domain.Base import Base

class ProductImage(Base):
    __tablename__ = "product_image"
    
    id: Mapped[int] = mapped_column(primary_key=True)
    image: Mapped[str] = mapped_column()
    size: Mapped[int] = mapped_column()
    content_type: Mapped[str] = mapped_column()
    is_default: Mapped[bool] = mapped_column()
    product_id: Mapped[int] = mapped_column()