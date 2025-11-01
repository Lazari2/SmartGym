from app import db
from datetime import datetime
from sqlalchemy.dialects.postgresql import UUID
import uuid

class Workout(db.Model):
    __tablename__ = 'workouts'

    id = db.Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    member_id = db.Column(UUID(as_uuid=True), db.ForeignKey('member_profile.id'), nullable=False)
    name = db.Column(db.String(120), nullable=False)
    weekday = db.Column(db.String(20), nullable=False) 
    created_at = db.Column(db.DateTime, default=datetime.utcnow)

    member = db.relationship('MemberProfile', backref='workouts')

    def __repr__(self):
        return f"<Workout {self.name} ({self.weekday})>"
    
    def to_dict(self):
        return {
            'id': str(self.id),
            'name': self.name,
            'weekday': self.weekday,
            'created_at': self.created_at.isoformat(),
            'exercises': [exercise.to_dict() for exercise in self.exercises]
        }
    
    def to_summary_dict(self):
        """
        Returns a summary to the home screen, with data for display and filtering
        """

        total_sets = 0
        try:
            for exercise in self.exercises:
                total_sets += exercise.sets
        except Exception as e:
            print(f"Warning: We were unable to calculate the sets for the workout. {self.id}. {e}")
            total_sets = 0 

        formatted_date = self.created_at.strftime("%d/%m/%Y")

        return {
            'id': str(self.id),
            'name': self.name,
            'date': formatted_date,   
            'total_sets': total_sets, 
            'weekday': self.weekday    
        }