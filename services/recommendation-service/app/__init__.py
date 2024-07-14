import os
import py_eureka_client.eureka_client as eureka_client
from flask import Flask
from flask_sqlalchemy import SQLAlchemy

project_dir = os.path.dirname(os.path.abspath(__file__))
app = Flask(__name__)

user = "root"
pin = "1234"
host = "localhost"
db_name = "core_user"

# user = "root"
# pin = "green-craze-be-v2"
# host = "green-craze-mysql"
# db_name = "core_user"
 
# Configuring database URI
app.config['SQLALCHEMY_DATABASE_URI'] = f"mysql+pymysql://{user}:{pin}@{host}/{db_name}"
 
# Disable modification tracking
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

db = SQLAlchemy(app)

# The flowing code will register your server to eureka server and also start to send heartbeat every 30 seconds
# eureka_server = "http://localhost:7230/eureka"
# eureka_server = "http://green-craze-eureka-service:7230/eureka"

eureka_client.init(eureka_server="http://localhost:7230/eureka",
                   app_name="recommendation-service",
                   instance_ip="127.0.0.1",
                   instance_port=5000)

from app.controller.controller import *