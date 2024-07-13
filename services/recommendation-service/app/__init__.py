import os
from flask import Flask
from flask_sqlalchemy import SQLAlchemy

project_dir = os.path.dirname(os.path.abspath(__file__))
app = Flask(__name__)

# user = "root"
# pin = "1234"
# host = "localhost"
# db_name = "core_user"

user = "root"
pin = "green-craze-be-v2"
host = "green-craze-mysql"
db_name = "core_user"
 
# Configuring database URI
app.config['SQLALCHEMY_DATABASE_URI'] = f"mysql+pymysql://{user}:{pin}@{host}/{db_name}"
 
# Disable modification tracking
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)

from app.controller.controller import *