from app import db
from datetime import datetime
from sqlalchemy.dialects.postgresql import UUID
import uuid

class Exercise(db.Model):
    __tablename__ = 'exercises'

    id = db.Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    workout_id = db.Column(UUID(as_uuid=True), db.ForeignKey('workouts.id'), nullable=False)
    exercise_template_id = db.Column(UUID(as_uuid=True), db.ForeignKey('exercise_templates.id'), nullable=False)
    sets = db.Column(db.Integer, nullable=False)
    reps = db.Column(db.Integer, nullable=False)
    weight = db.Column(db.Float)
    notes = db.Column(db.Text, nullable=True)
    image_url = db.Column(db.String(255))
    created_at = db.Column(db.DateTime, default=datetime.utcnow)

    workout = db.relationship('Workout', backref=db.backref('exercises', cascade="all, delete-orphan"))
    template = db.relationship('ExerciseTemplate')

    def __repr__(self):
        return f"<Exercise {self.template.name} - {self.sets}x{self.reps}>"
    
    def to_dict(self):
        return {
            'id': str(self.id),
            'sets': self.sets,
            'reps': self.reps,
            'weight': self.weight,
            'notes': self.notes,
            'template': self.template.to_dict() if self.template else None
        }