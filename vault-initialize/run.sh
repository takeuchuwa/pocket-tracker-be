#!/bin/bash

VAULT_RETRIES=5
echo "Vault is starting..."
until vault status >/dev/null 2>&1 || [ "$VAULT_RETRIES" -eq 0 ]; do
  echo "Waiting for vault to start...: $((VAULT_RETRIES--))"
  sleep 1
done

echo "Authenticating to vault..."
vault login token=e65e2ef0feac46bf9a4ae78cd236c81f

echo "Initializing vault databases..."
vault secrets enable -path=users_postgres database

echo "Adding entries..."
vault write users_postgres/config/postgresql \
  plugin_name="postgresql-database-plugin" \
  allowed_roles="user-rw,user-ro" \
  connection_url="postgresql://{{username}}:{{password}}@users-db:5432/users" \
  username="users_admin" \
  password="users_pass"

vault write users_postgres/roles/user-rw \
db_name="postgresql" \
creation_statements="CREATE ROLE \"{{name}}\" WITH SUPERUSER LOGIN PASSWORD '{{password}}' VALID UNTIL '{{expiration}}'; \
                            GRANT ALL ON SCHEMA public TO \"{{name}}\";" \
default_ttl="1h" \
max_ttl="24h"

vault write users_postgres/roles/user-ro \
    db_name="postgresql" \
    creation_statements="CREATE ROLE \"{{name}}\" WITH LOGIN PASSWORD '{{password}}' VALID UNTIL '{{expiration}}'; \
        GRANT SELECT ON ALL TABLES IN SCHEMA public TO \"{{name}}\";" \
    default_ttl="1h" \
    max_ttl="24h"

echo "Complete database..."

echo "Initializing vault secrets..."
vault secrets enable -version=2 -path=pocket_tracker kv

echo "Adding entries..."
vault kv put pocket_tracker/application app.jwt.sign.key="-----BEGIN PRIVATE KEY-----
MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDAHnI0pfTs+DAe
qbfSg01Qr+dU91yxS16jLAq7Up/l8+sQfpNwY4YgLDVLjH2bDzFHStDtXYuvc1A0
rf52gB2Kqn9CwKp7AR9YRNNuCFcXLxXby52mkYtTNVtxEL4ch0blfcoYJZc8Ukd5
5djX7O7jvJtF0HivlSA9cOxRSddOfp3qy4DUMhOp79tgFj34QoK3V8Bz++PBPVTZ
HaXqoX3SfgxlQ19MMjXGqLwk1DLhPTLhnNPLYtESXauE0NHsF/89Qj755aPnD5wX
n3zKDrotNOtxf4rd2OR02TMqrK5nQ2MMl6OObgu4mqHAsU/tD2ojT8F3Hdpe1g3A
4A7TR5LvAgMBAAECggEAXUdrE/zv6o6mbnzg76koxOBjZXGk48BAduFPZJw9Xp0L
qLqMQ6lO2hgc2E2iPsMlngPJZTZiOBG5A5rnTgnuTgFyQKrjG030NhtUvLv37R3D
czBtnw5To1boywDiDV/xIEPu98Jop7UbVyguHMjBRHmyujb6IeY5/Rry4pUDoOwo
kDFFUlYT6D6FYXgwsV5rg7QxFoCh41V9QmjTJcQ7Ml9EG+QeQZ+LOP1WZQqEQbJv
dmbzxGqjJGv2WKyi6s3kPvwj7io++DU8R9aAph4LLATm/fG7pBri1sd1nedlqSQi
7EQ2wrbcGeFLgsNOxAkSvd9qeBGV9T39YpogZLKRoQKBgQD24G9NloXIUSUqRDkH
6AMKVHKVdQlHXqpGxzgRZ4wfe6relIoG1JwwU1NocJ9Dd6UQasDXvYMIfsVoxtC5
EhmrrGvwzmFZD2nzxKoF4pzVmxdqMgGIKmtIbmEcxD9DEnKFb6RqzEW4T3MUwvPk
eVj7v1TjIye6zlFgFTRAD3vwDQKBgQDHN/pjziNxTUBdX7ii2t3j6w4xP+AKDuim
1YIpEipPg9OLkL8OeA3jhwYJs1zchT+QKaM7ZDqJmxmjaKoc2LMghQYJ3QJ/Rxab
5L+nZVj5cIzDOndWGWvxznSkFQiRRLX+d3dVJpTZiQje/OmoBZ9mQdnInCSbyu1f
0ef2SuZT6wKBgBeR6+57bYBnhuXXJ57Cnu+wilQMnXsr5ZPmV0sKGM20r+aaRedL
CzJt8iupfPsjBCZiGtE+LbF08QIE4U6aFMtYJeISwiaPmy0D3Lu9G/4k1YJ1DcJj
UmTS7QCjF+seasKkmwz7PbjQFQupZUGt+Dno1YDuDU//fLNwstFe6RVNAoGBALv+
6k3G2JFyGhxDWURtj9kmFwWIxo4XVZsifqrtVnok57ubw4AO8ORWpe7/kNEU+U+8
yyofwlHayI35FhcUs60revBR3KppVj+U1PtN23HAsUUGZlkcsk3EyzOQGb6kfa39
5OG4/9xswa9UMdb6P1rLJ451MuLF082JkwFL2yEzAoGAVcB31bGgA5ta4MhePfiu
oohgiFWZXZOAlMqcpK+TBxq3Y5/eFHNW1XykGAo5OkM7MkAC2kWLVMj7EP1/qaZY
b9BDqdhnQgG/WyZFFcSP7+Y0vinkzdYTtnXFn7qDhJANWoOngs5UG2eej/FJcDW0
9ArNKll96/n+V6MWhbYVXO4=
-----END PRIVATE KEY-----" \
app.redis.password="eYVX7EwVmmxKPCDmwMtyKVge8oLd2t81" \
app.jwt.public.key="-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwB5yNKX07PgwHqm30oNN
UK/nVPdcsUteoywKu1Kf5fPrEH6TcGOGICw1S4x9mw8xR0rQ7V2Lr3NQNK3+doAd
iqp/QsCqewEfWETTbghXFy8V28udppGLUzVbcRC+HIdG5X3KGCWXPFJHeeXY1+zu
47ybRdB4r5UgPXDsUUnXTn6d6suA1DITqe/bYBY9+EKCt1fAc/vjwT1U2R2l6qF9
0n4MZUNfTDI1xqi8JNQy4T0y4ZzTy2LREl2rhNDR7Bf/PUI++eWj5w+cF598yg66
LTTrcX+K3djkdNkzKqyuZ0NjDJejjm4LuJqhwLFP7Q9qI0/Bdx3aXtYNwOAO00eS
7wIDAQAB
-----END PUBLIC KEY-----"

echo "Complete secrets..."

echo "Complete"