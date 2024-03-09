FROM ubuntu:18.04

ENTRYPOINT []

RUN apt-get update && apt-get install -y python3 python3-pip wget

# Install CMake version 3.12 manually
RUN wget https://cmake.org/files/v3.12/cmake-3.12.0-Linux-x86_64.sh && \
    chmod +x cmake-3.12.0-Linux-x86_64.sh && \
    ./cmake-3.12.0-Linux-x86_64.sh --skip-license --prefix=/usr/local && \
    rm cmake-3.12.0-Linux-x86_64.sh

RUN python3 -m pip install --no-cache --upgrade pip

RUN pip3 install --no-cache dm-tree

ADD . /app/

RUN chmod +x /app/server.sh

CMD /app/server.sh
