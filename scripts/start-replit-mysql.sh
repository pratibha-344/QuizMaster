#!/usr/bin/env bash
set -euo pipefail

PROJECT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
DATA_ROOT="$PROJECT_DIR/.mysql-data"
DATA_DIR="$DATA_ROOT/data"
MYSQL_USER="$(id -un)"
MYSQLD="$(command -v mysqld)"
MYSQL_ADMIN="$(command -v mysqladmin)"

if ! command -v mysql >/dev/null 2>&1; then
  echo "MySQL client is missing. Install the mysql84 system package first." >&2
  exit 1
fi
MYSQL="$(command -v mysql)"

mkdir -p "$DATA_DIR"
if [[ ! -d "$DATA_DIR/mysql" ]]; then
  echo "Initializing the local QuizMaster MySQL data directory..."
  "$MYSQLD" --no-defaults --initialize-insecure --user="$MYSQL_USER" --datadir="$DATA_DIR"
fi

"$MYSQLD" \
  --no-defaults \
  --user="$MYSQL_USER" \
  --datadir="$DATA_DIR" \
  --socket="$DATA_DIR/mysql.sock" \
  --pid-file="$DATA_DIR/mysql.pid" \
  --port=3306 \
  --bind-address=127.0.0.1 \
  --mysqlx=0 \
  --log-error="$DATA_ROOT/mysql-error.log" &
MYSQLD_PID=$!

stop_mysql() {
  kill "$MYSQLD_PID" 2>/dev/null || true
  wait "$MYSQLD_PID" 2>/dev/null || true
}
trap stop_mysql EXIT INT TERM

ready=0
for _ in $(seq 1 60); do
  if ! kill -0 "$MYSQLD_PID" 2>/dev/null; then
    echo "MySQL exited during startup:" >&2
    cat "$DATA_ROOT/mysql-error.log" >&2 || true
    exit 1
  fi
  if "$MYSQL_ADMIN" --protocol=tcp --host=127.0.0.1 --port=3306 --user=root ping --silent >/dev/null 2>&1; then
    ready=1
    break
  fi
  sleep 1
done

if [[ "$ready" != 1 ]]; then
  echo "MySQL did not become ready within 60 seconds:" >&2
  cat "$DATA_ROOT/mysql-error.log" >&2 || true
  exit 1
fi

"$MYSQL" --protocol=tcp --host=127.0.0.1 --port=3306 --user=root < "$PROJECT_DIR/database.sql"
echo "QuizMaster MySQL is ready on 127.0.0.1:3306; database.sql has been applied."
wait "$MYSQLD_PID"
