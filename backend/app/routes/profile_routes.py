from flask import Blueprint, request, jsonify
from flask_jwt_extended import jwt_required, get_jwt_identity
from app import db
from app.utils.exceptions import AppError
from app.models.user import User
from app.models.memberProfile import MemberProfile

profile_bp = Blueprint('profile_bp', __name__, url_prefix='/api/profile')

@profile_bp.route('/', methods=['GET'])
@jwt_required()
def get_profile():
    current_user_id = get_jwt_identity()
    user = User.query.get(current_user_id)
    profile = MemberProfile.query.filter_by(user_id=current_user_id).first()

    if not user or not profile:
        raise AppError("Profile not found", 404)

    return jsonify(
        name=user.username,
        email=user.email,
        age=profile.age,
        weight=profile.weight,
        height=profile.height,
        goal=profile.description 
    ), 200

@profile_bp.route('/', methods=['PUT'])
@jwt_required()
def update_profile():
    current_user_id = get_jwt_identity()
    data = request.get_json()

    if not data:
        raise AppError("Error", 400)

    user = User.query.get(current_user_id)
    profile = MemberProfile.query.filter_by(user_id=current_user_id).first()

    if not user or not profile:
        raise AppError("Profile dont found.", 404)

    if 'name' in data:
        user.username = data['name']

    profile.age = data.get('age')
    profile.weight = data.get('weight')
    profile.height = data.get('height')
    profile.description = data.get('goal') 

    try:
        db.session.commit()
        return jsonify(message="Profile Updtated with Sucess"), 200
    except Exception as e:
        db.session.rollback()
        print(f"Error {e}")
        raise AppError("Error", 500)