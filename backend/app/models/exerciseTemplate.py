from app import db
from sqlalchemy.dialects.postgresql import UUID
import uuid

class ExerciseTemplate(db.Model):
    __tablename__ = 'exercise_templates'

    id = db.Column(UUID(as_uuid=True), primary_key=True, default=uuid.uuid4)
    name = db.Column(db.String(100), unique=True, nullable=False)
    muscle_group = db.Column(db.String(50), nullable=False, index=True) 
    description = db.Column(db.Text, nullable=True)
    image_url = db.Column(db.String(255), nullable=True)

    def __repr__(self):
        return f"<ExerciseTemplate '{self.name}' ({self.muscle_group})>"
    
    def to_dict(self):
        return {
            'id': str(self.id),
            'name': self.name,
            'muscle_group': self.muscle_group,
            'description': self.description,
            'image_url': self.image_url
        }
    
    def to_prompt_dict(self):
        return {
            'id': str(self.id),
            'name': self.name,
            'muscle_group': self.muscle_group,
            'description': self.description
        }