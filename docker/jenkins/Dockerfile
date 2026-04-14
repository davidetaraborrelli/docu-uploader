FROM jenkins/jenkins:lts-jdk21

# Salta il setup wizard iniziale
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"

# Configurazione automatica con JCasC
ENV CASC_JENKINS_CONFIG=/usr/local/jenkins-casc/jenkins.yml

# Installa plugin
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli --plugin-file /usr/share/jenkins/ref/plugins.txt

# Copia configurazione JCasC
COPY jenkins.yml /usr/local/jenkins-casc/jenkins.yml

# Installa tool di build come root
USER root

RUN apt-get update && \
    apt-get install -y --no-install-recommends maven nodejs npm docker.io && \
    rm -rf /var/lib/apt/lists/* && \
    usermod -aG docker jenkins

USER jenkins
