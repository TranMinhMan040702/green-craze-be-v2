
# user = "root"
# pin = "1234"
# host = "localhost"

user = "root"
pin = "green-craze-be-v2"
host = "green-craze-mysql"

core_user_connection_str = f"mysql+pymysql://{user}:{pin}@{host}/core_user"

core_product_connection_str = f"mysql+pymysql://{user}:{pin}@{host}/core_product"