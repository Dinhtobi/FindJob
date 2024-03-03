from flask import Flask , request
from flask_cors import CORS , cross_origin
from Controller.RecommendController import RecommendController

app = Flask(__name__)
CORS(app)

app.config['CORS_HEADERS'] = 'Content-Type'

app.register_blueprint(RecommendController)

if __name__ == '__main__':
    app.run(host='0.0.0.0' , port= '6868')


