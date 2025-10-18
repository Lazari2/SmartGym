from flask import Blueprint, jsonify, request
from flask_jwt_extended import jwt_required
from app.services.chat_service import ChatService
from app.utils.exceptions import AppError

chat_bp = Blueprint('chat_bp', __name__, url_prefix='/api/chat')

@chat_bp.route('', methods=['POST'])
@jwt_required()
def post_chat_message():
    """
    Endpoint to send a prompt to the chatbot..
    """
    data = request.get_json()
    
    if not data or 'prompt' not in data:
        raise AppError("The 'prompt' field is required", 400)
    
    try:
        user_prompt = data['prompt']

        ia_response = ChatService.get_chat_response(user_prompt)

        return jsonify({"response": ia_response}), 200
    
    except AppError as e:
        return jsonify({"error": e.message}), e.status_code
    except Exception as e:
        return jsonify({"error": f"An unexpected error occurs: {str(e)}"}), 500