from app import create_app, db
from app.models.user import User
from app.models.exercise import Exercise
from app.models.memberProfile import MemberProfile
from app.models.workout import Workout

app = create_app()

@app.shell_context_processor
def make_shell_context():
    return {
        'app': app,
        'db': db,
        'User': User,
        'MemberProfile': MemberProfile,
        'Workout': Workout,
        'Exercise': Exercise
        }

if __name__ == '__main__':
    app.run(debug=True)