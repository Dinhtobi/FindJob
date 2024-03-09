FROM ubuntu:22.04
ENTRYPOINT []
RUN apt-get update && apt-get install -y python3 python3-pip && python3 -m pip install --no-cache --upgrade pip
RUN pip3 install --no-cache rasa
RUN pip3 install --no-cache pandas
ADD . /app/
RUN chmod +r /app/jobs_list.pkl
RUN chmod +r /app/similarity.pkl

RUN chmod +x /app/server.sh
CMD /app/server.sh