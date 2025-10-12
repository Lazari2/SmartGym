from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
from flask_jwt_extended import JWTManager
from flask_migrate import Migrate

db = SQLAlchemy()
jwt = JWTManager()
migrate = Migrate()

def create_app(): 
    app = Flask(__name__)
    app.config.from_object('app.config.Config') 
    
    db.init_app(app)
    # jwt.init_app(app) 
    migrate.init_app(app, db)

    # from app.routes.aluno_routes import aluno_bp
    # from app.routes.treino_routes import treino_bp

    # app.register_blueprint(aluno_bp, url_prefix='/aluno')
    # app.register_blueprint(treino_bp, url_prefix='/treino')

    return app