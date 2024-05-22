

class ProductResponse:
    def __init__(self, product, brand, category, unit, product_images, variants):
        self.id = product.id
        self.name = product.name
        self.shortDescription = product.short_description
        self.description = product.description
        self.code = product.code
        self.quantity = product.quantity
        self.actualInventory = product.actual_inventory
        self.sold = product.sold
        self.rating = product.rating
        self.slug = product.slug
        self.code = product.code
        self.status = product.status
        self.unit = UnitResponse(unit).__dict__
        self.brand = BrandResponse(brand).__dict__
        self.productCategory = ProductCategoryResponse(category).__dict__
        self.images = [ProductImageResponse(image).__dict__ for image in product_images]
        self.variants = [VariantResource(variant).__dict__ for variant in variants]
        

class UnitResponse:
    def __init__(self, unit):
        self.id = unit.id
        self.name = unit.name
        self.status = unit.status
        
class ProductCategoryResponse:
    def __init__(self, category):
        self.id = category.id
        self.name = category.name
        self.parentId = category.parent_id
        self.slug = category.slug
        self.image = category.image
        self.status = category.status
        
class BrandResponse:
    def __init__(self, brand):
        self.id = brand.id
        self.name = brand.name
        self.code = brand.code
        self.description = brand.description
        self.image = brand.image
        self.status = brand.status

class ProductImageResponse:
    def __init__(self, product_image):
        self.id = product_image.id
        self.image = product_image.image
        self.productId = product_image.product_id
        self.isDefault = product_image.is_default

class VariantResource:
    def __init__(self, variant):
        self.id =  variant.id
        self.name = variant.name
        self.sku = variant.sku
        self.quantity = variant.quantity
        self.itemPrice = variant.item_price
        self.totalPrice = variant.total_price
        self.totalPromotionalPrice = variant.promotional_item_price
        self.promotionalItemPrice = variant.total_promotional_price
        self.status = variant.status