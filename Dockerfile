FROM ubuntu:18.04

ENTRYPOINT []

RUN apt-get update && apt-get install -y python3 python3-pip cmake && python3 -m pip install --no-cache --upgrade pip

# Additional checks and dependencies
RUN cmake --version

RUN pip3 install --no-cache dm-tree==<specific_version>

ADD . /app/

RUN chmod +x /app/server.sh

CMD /app/server.sh
