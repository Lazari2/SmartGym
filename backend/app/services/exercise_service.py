from app import db
from app.models.exerciseTemplate import ExerciseTemplate

class ExerciseService:

    @staticmethod
    def get_muscle_groups():
        """
        Searches all unique muscle groups in the exercise library,
        in alphabetical order.
        """
        try:

            results = (
                db.session.query(ExerciseTemplate.muscle_group)
                .distinct()
                .order_by(ExerciseTemplate.muscle_group)
                .all()
            )

            muscle_groups = [group[0] for group in results]
            
            return muscle_groups
        
        except Exception as e:
            print(f"Error searching for muscle groups: {e}")
            raise ValueError("Error retrieving muscle group data.")
    
    @staticmethod
    def get_exercises_by_group(group_name):
        """Search for all exercises for a specific muscle group."""
        try:
            exercises = (
                ExerciseTemplate.query
                .filter(ExerciseTemplate.muscle_group == group_name) 
                .order_by(ExerciseTemplate.name) 
                .all()
            )

            return [exercise.to_dict() for exercise in exercises]
        
        except Exception as e:
            print(f"Error searching for exercises by group: {e}")
            raise ValueError("Error retrieving exercise data.")