from app import db
from datetime import datetime
from sqlalchemy.dialects.postgresql import UUID
import uuid

class Exercise(db.Model):
    __tablename__ = 'exercises'

    id = db.Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    workout_id = db.Column(UUID(as_uuid=True), db.ForeignKey('workouts.id'), nullable=False)
    name = db.Column(db.String(100), nullable=False)
    sets = db.Column(db.Integer, nullable=False)
    reps = db.Column(db.Integer, nullable=False)
    weight = db.Column(db.Float)
    image_url = db.Column(db.String(255))
    created_at = db.Column(db.DateTime, default=datetime.utcnow)

    workout = db.relationship('Workout', backref='exercises')

    def __repr__(self):
        return f"<Exercise {self.name} - {self.sets}x{self.reps}>"