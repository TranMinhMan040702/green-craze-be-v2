from sqlalchemy.orm import Mapped
from sqlalchemy.orm import mapped_column
from app.domain.Base import Base

class Brand(Base):
    __tablename__ = "brand"
    
    id: Mapped[int] = mapped_column(primary_key=True)
    name: Mapped[str] = mapped_column()
    code: Mapped[str] = mapped_column()
    description: Mapped[str] = mapped_column()
    image: Mapped[str] = mapped_column()
    status: Mapped[bool] = mapped_column()