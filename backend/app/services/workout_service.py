from app import db
from app.models.workout import Workout
from app.models.exercise import Exercise
from app.models.memberProfile import MemberProfile
from app.utils.exceptions import AppError  # (Vamos precisar criar este arquivo)
import uuid

class WorkoutService:

    @staticmethod
    def create_workout(user_id, data):
        """
        Creates a new complete workout for a user.
        'user_id' comes from the JWT token.
        'data' is the JSON sent by the front-end.
        """

        required_fields = ['name', 'weekday', 'exercises']
        if not all(field in data for field in required_fields):
            raise AppError("Incomplete data: 'name', 'weekday', and 'exercises' are required.", 400)
            
        if not data['exercises']:
            raise AppError("The workout must have at least one exercise.", 400)

        # 2. Encontrar o Perfil do Membro
        # ASSUMINDO que o 'member_profile.id' é o mesmo que o 'user.id'
        # DEPOIS QUE CONSTRUIR O PERFIL DO USUARIO TROCA PARA ->  user_id = db.ForeignKey('user.id')
        # Por hora busca o perfil pelo ID do usuário logado
        member_id = user_id # SIMPLIFICANDO
        
        # Para testar, precisamos garantir que um MemberProfile com este ID exista!
        # Você pode precisar inserir um manualmente no DBeaver por enquanto.
        profile = MemberProfile.query.get(member_id)
        if not profile:
            raise AppError(f"No member profiles found for the user. (ID: {member_id})", 404)

        try:
            new_workout = Workout(
                member_id=member_id,
                name=data['name'],
                weekday=data['weekday']
            )
            db.session.add(new_workout)

            db.session.flush()

            exercises_list = []
            for ex_data in data['exercises']:
                if not 'template_id' in ex_data or not 'sets' in ex_data or not 'reps' in ex_data:
                    raise AppError("Each exercise must have 'template_id', 'sets', e 'reps'.", 400)
                
                new_exercise = Exercise(
                    workout_id=new_workout.id,
                    exercise_template_id=ex_data['template_id'],
                    sets=ex_data['sets'],
                    reps=ex_data['reps'],
                    weight=ex_data.get('weight'),
                    notes=ex_data.get('notes')
                )
                exercises_list.append(new_exercise)

            db.session.add_all(exercises_list)

            db.session.commit()
            
            return new_workout 

        except Exception as e:
            db.session.rollback() 
            print(f"Error creating workout: {e}")
            raise AppError(f"Internal error while saving training. {e}", 500)
    
    @staticmethod
    def get_workouts_for_user(user_id):
        """Searches for all workouts associated with a user (by member_id)"""
        
        # VAMOS MANTER MEMBER_ID = USER_ID ATÉ CONSTRUIR O PERFIL DO USUARIO
        member_id = user_id
        
        try:
            workouts = Workout.query.filter_by(member_id=member_id).order_by(Workout.created_at.desc()).all()

            return [workout.to_summary_dict() for workout in workouts]
        except Exception as e:
            print(f"Error fetching user workouts: {e}")
            raise AppError("Error searching for workouts.", 500)
    
    @staticmethod
    def get_workout_details(user_id, workout_id):
        """Search for details of a specific workout."""
        
        # VAMOS MANTER MEMBER_ID = USER_ID ATÉ CONSTRUIR O PERFIL DO USUARIO
        member_id = user_id

        try:
            workout = Workout.query.get(workout_id) 
            if not workout:
                raise AppError("Workout not found", 404)

            if str(workout.member_id) != member_id:
                raise AppError("Unauthorized access to this workout.", 403) 

            return workout.to_dict()
        except AppError as e:
            raise e 
        except Exception as e:
            print(f"Error fetching workout details. {e}")
            raise AppError("Error fetching workout details.", 500)