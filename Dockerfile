FROM ubuntu:22.04

FROM python:3.9-slim
ENTRYPOINT []
RUN apt-get update && apt-get install -y python3 python3-pip && python3 -m pip install --no-cache --upgrade pip
RUN pip3 install --no-cache rasa
RUN pip3 install --no-cache pandas
RUN pip3 install --no-cache pickle=4.0
ADD . /app/

RUN chmod +x /app/server.sh
CMD /app/server.sh