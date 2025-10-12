from app import db
from app.models.user import User
from app.utils.jwt_utils import generate_jwt
from werkzeug.security import generate_password_hash, check_password_hash

class AuthService:

    @staticmethod
    def register_user(username, email, password, confirm_password):
        if password != confirm_password:
            raise ValueError("Passwords do not match.")
        if User.query.filter((User.username == username) | (User.email == email)).first():
            raise ValueError("User or email already exists.")

        user = User(username=username, email=email)
        user.set_password(password)
        db.session.add(user)
        db.session.commit()
        return user

    @staticmethod
    def login_user(email, password):
        user = User.query.filter_by(email=email).first()
        if not user or not user.check_password(password):
            raise ValueError("Invalid email or password.")

        token = generate_jwt(user.id)
        return token