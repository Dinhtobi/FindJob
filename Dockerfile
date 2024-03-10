
FROM python:3.9-slim
ENTRYPOINT []
RUN apt-get update && apt-get install -y python3 python3-pip && python3 -m pip install --no-cache --upgrade pip
RUN pip3 install --no-cache rasa==3.1
RUN pip3 install --no-cache pandas
RUN pip3 install --no-cache flask
RUN pip3 install --no-cache flask flask_cors
RUN pip3 install websockets==10.0
ADD . /app/

# WORKDIR /app
COPY data /app/data

RUN chmod +x /app/server.sh
CMD /app/server.sh