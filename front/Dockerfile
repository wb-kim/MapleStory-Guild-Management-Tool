# nginx최신 stable 버전 컨테이너를 커스텀
FROM nginx:latest
 
# 기존 설정파일 삭제
RUN rm /etc/nginx/nginx.conf
# 수정한 설정파일을 복사
COPY nginx.conf /etc/nginx/nginx.conf
COPY . /usr/share/nginx/html
# nginx 기동
CMD ["nginx", "-g", "daemon off;"]
 
# 포트는 80
EXPOSE 80
EXPOSE 443