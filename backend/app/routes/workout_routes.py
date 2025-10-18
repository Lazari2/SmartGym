from flask import Blueprint, jsonify, request
from flask_jwt_extended import jwt_required, get_jwt_identity
from app.services.workout_service import WorkoutService
from app.utils.exceptions import AppError

workout_bp = Blueprint('workout_bp', __name__, url_prefix='/api/workouts')

@workout_bp.route('', methods=['POST'])
@jwt_required()
def create_new_workout():
    """
    Endpoint para criar um novo treino completo com seus exercícios.
    """
    try:
        user_id = get_jwt_identity()
        data = request.get_json()
        new_workout = WorkoutService.create_workout(user_id, data)

        return jsonify(new_workout.to_dict()), 201 

    except AppError as e:
        return jsonify({"error": e.message}), e.status_code
    except Exception as e:
        return jsonify({"error": f"Unexpected error: {str(e)}"}), 500

@workout_bp.route('', methods=['GET'])
@jwt_required()
def get_user_workouts():
    """
    Endpoint to list all workouts of the logged in user.
    """
    try:
        user_id = get_jwt_identity()
        workouts = WorkoutService.get_workouts_for_user(user_id)
        return jsonify(workouts), 200
    except AppError as e:
        return jsonify({"error": e.message}), e.status_code
    except Exception as e:
        return jsonify({"error": f"An unexpected error: {str(e)}"}), 500

@workout_bp.route('/<uuid:workout_id>', methods=['GET'])
@jwt_required()
def get_single_workout(workout_id):
    """
    Endpoint to fetch the details of a single workout by its ID.
    """
    try:
        user_id = get_jwt_identity()
        workout = WorkoutService.get_workout_details(user_id, str(workout_id))
        
        return jsonify(workout), 200
    except AppError as e:
        return jsonify({"error": e.message}), e.status_code
    except Exception as e:
        return jsonify({"error": f"Um erro inesperado ocorreu: {str(e)}"}), 500