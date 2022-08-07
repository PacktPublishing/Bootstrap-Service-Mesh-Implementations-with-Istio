FROM mysql:latest

# ENV MYSQL_ROOT_PASSWORD DEFAULT_PASS
# ENV MYSQL_ALLOW_EMPTY_PASSWORD=true
# ENV MYSQL_DATABASE=socksdb

COPY ./data/dump.sql /docker-entrypoint-initdb.d/
ENV MYSQL_ROOT_PASSWORD=default_password
EXPOSE 3306 33060
ENTRYPOINT ["docker-entrypoint.sh"]
CMD ["mysqld","--default-authentication-plugin=mysql_native_password"]

