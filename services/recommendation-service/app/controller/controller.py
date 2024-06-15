from flask import request, jsonify
from app import app
from werkzeug.exceptions import HTTPException

from app.middleware.auth_middleware import token_required
import app.services.recommendation_service as rs

from app.models.Response import Response

@app.errorhandler(Exception)
def handle_error(e):
    code = 500
    if isinstance(e, HTTPException):
        code = e.code
    return jsonify(error=str(e)), code

@app.route('/api/recommendations/user', methods=['GET'])
@token_required
def get_recommendations_for_current_user(current_user_id):
    print(current_user_id)
    product_recommendations = rs.get_recommendations_by_user(current_user_id)
    
    return jsonify(Response(200, product_recommendations).__dict__)


@app.route('/api/recommendations/product/<product_id>', methods=['GET'])
def get_recommendations_for_product(product_id):
    product_recommendations = rs.get_recommendations_by_product(product_id)
    if product_recommendations is None:
        return jsonify(Response(404, "Product not found").__dict__)
    
    return jsonify(Response(200, product_recommendations).__dict__)