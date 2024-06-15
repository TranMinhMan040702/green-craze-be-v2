from functools import wraps
import jwt
from flask import request, abort

def token_required(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        token = None
        if "Authorization" in request.headers:
            token = request.headers["Authorization"].split(" ")[1]
        if not token:
            return {
                "message": "Authentication Token is missing!",
                "data": None,
                "error": "Unauthorized"
            }, 401
        try:
            data=jwt.decode(token, options={"verify_signature": False})
            current_user_id = data["userId"]
            if current_user_id is None:
                return {
                    "message": "Invalid Authentication token!",
                    "data": None,
                    "error": "Unauthorized"
                }, 401
        except Exception as e:
            return {
                "message": "Internal Server",
                "data": None,
                "error": str(e)
            }, 500

        return f(current_user_id, *args, **kwargs)

    return decorated