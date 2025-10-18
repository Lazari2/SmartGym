from flask import Blueprint, jsonify
from flask_jwt_extended import jwt_required
from app.services.exercise_service import ExerciseService

exercise_bp = Blueprint('exercise_bp', __name__, url_prefix='/api/exercises')

@exercise_bp.route('/groups', methods=['GET'])
@jwt_required()  
def get_all_muscle_groups():
    """
    Endpoint to list all unique categories (muscle groups).
    """
    try:
        groups = ExerciseService.get_muscle_groups()
        return jsonify(groups), 200
    except ValueError as e:
        return jsonify({"error": str(e)}), 500

@exercise_bp.route('/group/<string:group_name>', methods=['GET'])
@jwt_required()
def get_exercises_for_group(group_name):
    """
    Endpoint to list all exercises for a specific muscle group.
    """
    try:
        exercises = ExerciseService.get_exercises_by_group(group_name)
        return jsonify(exercises), 200
    except ValueError as e:
        return jsonify({"error": str(e)}), 500 