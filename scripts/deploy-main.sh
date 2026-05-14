#!/usr/bin/env bash
set -euo pipefail

APP_DIR="${APP_DIR:-$HOME/school-predict}"
BRANCH="${BRANCH:-main}"
COMPOSE_FILE="${COMPOSE_FILE:-docker-compose.prod.yml}"
LOCK_FILE="/tmp/school-predict-deploy.lock"

exec 9>"$LOCK_FILE"
if ! flock -n 9; then
  exit 0
fi

cd "$APP_DIR"

if [ ! -f ".env" ]; then
  echo ".env file is missing in $APP_DIR" >&2
  exit 1
fi

CURRENT_COMMIT="$(git rev-parse HEAD)"
git fetch origin "$BRANCH" --quiet
REMOTE_COMMIT="$(git rev-parse "origin/$BRANCH")"

if [ "$CURRENT_COMMIT" = "$REMOTE_COMMIT" ]; then
  exit 0
fi

git reset --hard "origin/$BRANCH"
docker compose -f "$COMPOSE_FILE" up -d --build
docker image prune -f

echo "Deployed $REMOTE_COMMIT"
