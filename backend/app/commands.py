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
        # // ----------------------------------------------------
        # // --- PEITO / CHEST (Peitoral, Deltoide Anterior, Tríceps) ---
        # // ----------------------------------------------------
        {'name': 'Supino Reto com Barra', 'muscle_group': 'Peito'},
        {'name': 'Crucifixo com Halteres', 'muscle_group': 'Peito'},
        {'name': 'Flexão de Braço (Push-up)', 'muscle_group': 'Peito'},
        {'name': 'Supino Inclinado com Halteres', 'muscle_group': 'Peito'},
        {'name': 'Supino Reto com Halteres', 'muscle_group': 'Peito'},
        {'name': 'Supino Inclinado com Barra', 'muscle_group': 'Peito'},
        {'name': 'Supino Declinado com Barra', 'muscle_group': 'Peito'},
        {'name': 'Supino Declinado com Halteres', 'muscle_group': 'Peito'},
        {'name': 'Crucifixo Inclinado com Halteres', 'muscle_group': 'Peito'},
        {'name': 'Crossover (Polia Alta)', 'muscle_group': 'Peito'},
        {'name': 'Crossover (Polia Média)', 'muscle_group': 'Peito'},
        {'name': 'Crossover (Polia Baixa)', 'muscle_group': 'Peito'},
        {'name': 'Voador (Peck Deck)', 'muscle_group': 'Peito'},
        {'name': 'Fly Inclinado na Máquina', 'muscle_group': 'Peito'},
        {'name': 'Flexão de Braço (Mãos Fechadas / Diamante)', 'muscle_group': 'Peito'},
        {'name': 'Mergulho em Barras Paralelas (Foco Peito)', 'muscle_group': 'Peito'},
        {'name': 'Pull-over com Halter', 'muscle_group': 'Peito'},
        {'name': 'Supino com Pegada Fechada (Foco Tríceps/Peito)', 'muscle_group': 'Peito'},
        
        # // ----------------------------------------------------
        # // --- COSTAS / BACK (Dorsais, Trapézio, Romboides, Lombar) ---
        # // ----------------------------------------------------
        {'name': 'Puxada Frontal (Pulley)', 'muscle_group': 'Costas'},
        {'name': 'Remada Curvada com Barra', 'muscle_group': 'Costas'},
        {'name': 'Barra Fixa (Pull-up)', 'muscle_group': 'Costas'},
        {'name': 'Remada Unilateral com Halter (Serrote)', 'muscle_group': 'Costas'},
        {'name': 'Remada Baixa (Triângulo)', 'muscle_group': 'Costas'},
        {'name': 'Remada Baixa (Pegada Aberta)', 'muscle_group': 'Costas'},
        {'name': 'Puxada Supinada (Pulley Inverso)', 'muscle_group': 'Costas'},
        {'name': 'Remada no Cabo (Cavalinho)', 'muscle_group': 'Costas'},
        {'name': 'Remada Inclinada com Halteres', 'muscle_group': 'Costas'},
        {'name': 'Puxada Alta com Barra V', 'muscle_group': 'Costas'},
        {'name': 'Pullover na Polia Alta', 'muscle_group': 'Costas'},
        {'name': 'Levantamento Terra (Deadlift)', 'muscle_group': 'Costas'},
        {'name': 'Hiperextensão Lombar', 'muscle_group': 'Costas'},
        {'name': 'Barra Fixa (Pegada Neutra)', 'muscle_group': 'Costas'},

        # // ----------------------------------------------------
        # // --- PERNAS / LEGS (Quadríceps, Isquiotibiais, Glúteos) ---
        # // ----------------------------------------------------
        {'name': 'Agachamento Livre com Barra', 'muscle_group': 'Pernas'},
        {'name': 'Leg Press 45', 'muscle_group': 'Pernas'},
        {'name': 'Cadeira Extensora', 'muscle_group': 'Pernas'},
        {'name': 'Mesa Flexora', 'muscle_group': 'Pernas'},
        {'name': 'Afundo (Avanço) com Halteres', 'muscle_group': 'Pernas'},
        {'name': 'Cadeira Flexora', 'muscle_group': 'Pernas'},
        {'name': 'Stiff com Barra', 'muscle_group': 'Pernas'},
        {'name': 'Agachamento Sumô', 'muscle_group': 'Pernas'},
        {'name': 'Agachamento Búlgaro', 'muscle_group': 'Pernas'},
        {'name': 'Agachamento Frontal', 'muscle_group': 'Pernas'},
        {'name': 'Hack Squat', 'muscle_group': 'Pernas'},
        {'name': 'Leg Press Horizontal', 'muscle_group': 'Pernas'},
        {'name': 'Elevação Pélvica (Hip Thrust) com Barra', 'muscle_group': 'Pernas'},
        {'name': 'Coice na Polia (Glúteo)', 'muscle_group': 'Pernas'},
        {'name': 'Cadeira Adutora', 'muscle_group': 'Pernas'},
        {'name': 'Cadeira Abdutora', 'muscle_group': 'Pernas'},
        {'name': 'Passada (Lunge) com Barra', 'muscle_group': 'Pernas'},
        {'name': 'Good Morning', 'muscle_group': 'Pernas'},

        # // ----------------------------------------------------
        # // --- OMBROS / SHOULDERS (Deltoide Anterior, Lateral, Posterior, Trapézio) ---
        # // ----------------------------------------------------
        {'name': 'Desenvolvimento Militar (Barra em Pé)', 'muscle_group': 'Ombros'},
        {'name': 'Elevação Lateral com Halteres', 'muscle_group': 'Ombros'},
        {'name': 'Desenvolvimento com Halteres (Sentado)', 'muscle_group': 'Ombros'},
        {'name': 'Elevação Frontal com Halteres', 'muscle_group': 'Ombros'},
        {'name': 'Remada Alta com Barra', 'muscle_group': 'Ombros'},
        {'name': 'Crucifixo Inverso (Máquina ou Halteres)', 'muscle_group': 'Ombros'},
        {'name': 'Face Pull na Polia', 'muscle_group': 'Ombros'},
        {'name': 'Encolhimento de Ombros com Halteres (Trapézio)', 'muscle_group': 'Ombros'},
        {'name': 'Desenvolvimento Arnold', 'muscle_group': 'Ombros'},
        {'name': 'Remada Alta na Polia', 'muscle_group': 'Ombros'},
        {'name': 'Elevação Lateral no Cabo (Unilateral)', 'muscle_group': 'Ombros'},

        # // ----------------------------------------------------
        # // --- BÍCEPS / BICEPS (Bíceps Braquial, Braquial, Braquiorradial) ---
        # // ----------------------------------------------------
        {'name': 'Rosca Direta com Barra', 'muscle_group': 'Bíceps'},
        {'name': 'Rosca Alternada com Halteres', 'muscle_group': 'Bíceps'},
        {'name': 'Rosca Scott com Barra W', 'muscle_group': 'Bíceps'},
        {'name': 'Rosca Concentrada com Halter', 'muscle_group': 'Bíceps'},
        {'name': 'Rosca Martelo (Halteres)', 'muscle_group': 'Bíceps'},
        {'name': 'Rosca Inclinada com Halteres', 'muscle_group': 'Bíceps'},
        {'name': 'Rosca no Cabo (Polia Baixa)', 'muscle_group': 'Bíceps'},
        {'name': 'Rosca Inversa (Foco Antebraço)', 'muscle_group': 'Bíceps'},
        {'name': 'Rosca Hamer no Cabo', 'muscle_group': 'Bíceps'},
        {'name': 'Rosca 21 (Barra)', 'muscle_group': 'Bíceps'},
        
        # // ----------------------------------------------------
        # // --- TRÍCEPS / TRICEPS (Tríceps Braquial) ---
        # // ----------------------------------------------------
        {'name': 'Tríceps Pulley (Corda)', 'muscle_group': 'Tríceps'},
        {'name': 'Tríceps Testa com Barra W', 'muscle_group': 'Tríceps'},
        {'name': 'Tríceps Coice com Halter', 'muscle_group': 'Tríceps'},
        {'name': 'Tríceps Francês com Halter (Sentado)', 'muscle_group': 'Tríceps'},
        {'name': 'Tríceps Pulley (Barra Reta)', 'muscle_group': 'Tríceps'},
        {'name': 'Tríceps Pulley (Barra V)', 'muscle_group': 'Tríceps'},
        {'name': 'Mergulho em Barras Paralelas (Foco Tríceps)', 'muscle_group': 'Tríceps'},
        {'name': 'Tríceps no Banco (Dips)', 'muscle_group': 'Tríceps'},
        {'name': 'Extensão de Tríceps Acima da Cabeça (Cabo)', 'muscle_group': 'Tríceps'},
        {'name': 'Tríceps Supinado no Cabo', 'muscle_group': 'Tríceps'},

        # // ----------------------------------------------------
        # // --- CORE / ABDÔMEN (Reto Abdominal, Oblíquos, Transverso) ---
        # // ----------------------------------------------------
        {'name': 'Abdominal Clássico (Supra)', 'muscle_group': 'Core'},
        {'name': 'Abdominal Infra', 'muscle_group': 'Core'},
        {'name': 'Abdominal Remador', 'muscle_group': 'Core'},
        {'name': 'Prancha Frontal (Plank)', 'muscle_group': 'Core'},
        {'name': 'Prancha Lateral', 'muscle_group': 'Core'},
        {'name': 'Abdominal Obliquo (Bicicleta)', 'muscle_group': 'Core'},
        {'name': 'Elevação de Pernas Suspenso', 'muscle_group': 'Core'},
        {'name': 'Roda de Abdominal', 'muscle_group': 'Core'},
        {'name': 'Canivete (Jackknife)', 'muscle_group': 'Core'},
        {'name': 'Abdominal na Polia Alta (Crunch)', 'muscle_group': 'Core'},
        {'name': 'Twist Russo (Russian Twist)', 'muscle_group': 'Core'},
        {'name': 'Farmer’s Walk (Caminhada do Fazendeiro)', 'muscle_group': 'Core'},
        
        # // ----------------------------------------------------
        # // --- PANTURRILHA / CALVES (Gastrocnêmio, Sóleo) ---
        # // ----------------------------------------------------
        {'name': 'Elevação de Panturrilha em Pé (Máquina)', 'muscle_group': 'Panturrilha'},
        {'name': 'Elevação de Panturrilha no Leg Press', 'muscle_group': 'Panturrilha'},
        {'name': 'Panturrilha Sentado (Máquina Sóleo)', 'muscle_group': 'Panturrilha'},
        {'name': 'Elevação de Panturrilha Unilateral', 'muscle_group': 'Panturrilha'},
        {'name': 'Elevação de Panturrilha no Degrau', 'muscle_group': 'Panturrilha'}
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