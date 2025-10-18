import google.generativeai as genai
from app.utils.exceptions import AppError

class ChatService:
    
    @staticmethod
    def configure_gemini(api_key):
        """Configures the Gemini API. Must be called at app startup."""
        if api_key:
            genai.configure(api_key=api_key)
        else:
            print("Gemini API key not configured.")

    @staticmethod
    def get_chat_response(prompt):
        """Sends a prompt and the bot returns the text response."""
        try:
            model = genai.GenerativeModel('models/gemini-2.5-pro')
            
            persona = (
                "You are the 'SmartGym AI,' an expert personal trainer"
                "Be succinct, motivating, and to the point"
                "Respond in bullet points or short lists. Avoid long paragraphs"
                "Your goal is to provide practical workout suggestions"
                "Format your response clearly and easily"
                "You always respond in Brazilian Portuguese"
            )

            response = model.generate_content([persona, prompt])
            
            return response.text
        
        except Exception as e:
            print(f"Gemini API Error: {e}")
            raise AppError(f"API down {e}", 503) 