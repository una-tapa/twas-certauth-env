FROM ibmcom/websphere-traditional
ENV ENABLE_BASIC_LOGGING=true

USER root
RUN apt-get update
# RUN apt-get install -y emacs
# RUN apt-get install -y vim
RUN apt-get -y install openssh-client
COPY --chown=was:root tmp/PASSWORD /tmp/PASSWORD
COPY --chown=was:root work/config/configure.py /work/config/configure.py
COPY --chown=was:root CertLoginSample.war /work/app/
COPY --chown=was:root docker-entrypoint.sh /
COPY CustomUserMapping.jar /opt/IBM/WebSphere/AppServer/lib/ext
RUN chmod a+x /docker-entrypoint.sh
RUN /work/configure.sh

# ENTRYPOINT ["/docker-entrypoint.sh"]