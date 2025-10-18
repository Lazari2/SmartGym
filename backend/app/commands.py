import click
from flask.cli import with_appcontext
from app import db
from app.models.exerciseTemplate import ExerciseTemplate

@click.command(name='seed-db')
@with_appcontext
def seed_db():
    """
    Popula a tabela exercise_templates com dados de exemplo.
    """

    exercises = [
        {'name': 'Supino Reto com Barra', 'muscle_group': 'Peito'},
        {'name': 'Crucifixo com Halteres', 'muscle_group': 'Peito'},
        {'name': 'Flexão de Braço', 'muscle_group': 'Peito'},
        
        {'name': 'Puxada Frontal (Pulley)', 'muscle_group': 'Costas'},
        {'name': 'Remada Curvada com Barra', 'muscle_group': 'Costas'},
        {'name': 'Barra Fixa', 'muscle_group': 'Costas'},

        {'name': 'Agachamento Livre', 'muscle_group': 'Pernas'},
        {'name': 'Leg Press 45', 'muscle_group': 'Pernas'},
        {'name': 'Cadeira Extensora', 'muscle_group': 'Pernas'},
        {'name': 'Mesa Flexora', 'muscle_group': 'Pernas'},

        {'name': 'Desenvolvimento Militar (Barra)', 'muscle_group': 'Ombros'},
        {'name': 'Elevação Lateral com Halteres', 'muscle_group': 'Ombros'},
        
        {'name': 'Rosca Direta com Barra', 'muscle_group': 'Bíceps'},
        {'name': 'Rosca Alternada com Halteres', 'muscle_group': 'Bíceps'},
        
        {'name': 'Tríceps Pulley (Corda)', 'muscle_group': 'Tríceps'},
        {'name': 'Tríceps Testa', 'muscle_group': 'Tríceps'},
    ]
    
    print("Populando o banco de dados com exercícios de exemplo...")
    
    count = 0
    for ex_data in exercises:
        exists = ExerciseTemplate.query.filter_by(name=ex_data['name']).first()
        if not exists:
            new_exercise = ExerciseTemplate(
                name=ex_data['name'],
                muscle_group=ex_data['muscle_group']
            )
            db.session.add(new_exercise)
            count += 1

    try:
        db.session.commit()
        print(f"Sucesso! {count} novos exercícios foram adicionados.")
        print(f"Total de {len(exercises)} exercícios verificados.")
    except Exception as e:
        db.session.rollback()
        print(f"Erro ao popular o banco: {e}")