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