# Expedition API — Documentação de Endpoints

Base URL: `https://<seu-servidor>/api/v1`

Todos os endpoints requerem o header:
```
Authorization: Bearer <token>
```

---

## 📋 Sumário

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/expeditions` | Criar expedição |
| `GET` | `/expeditions` | Listar minhas expedições |
| `GET` | `/expeditions/{id}` | Detalhar expedição |
| `PUT` | `/expeditions/{id}` | Editar expedição (dono) |
| `DELETE` | `/expeditions/{id}` | Excluir expedição (dono) |
| `POST` | `/expeditions/{id}/invites` | Convidar amigo |
| `PATCH` | `/expeditions/invites/{inviteId}/respond` | Aceitar ou rejeitar convite |
| `GET` | `/expeditions/invites/pending` | Listar convites pendentes |

---

## 1. Criar Expedição

**`POST /expeditions`**

### Request Body
```json
{
  "title": "Mystic Mountain Trek",
  "osmId": 11379327,
  "nameTrail": "Trilha do Pico Paraná",
  "tags": {
    "route": "hiking",
    "sac_scale": "demanding_mountain_hiking",
    "network": "lwn"
  },
  "geometry": [
    { "lat": -25.2221393, "lon": -48.8563662 },
    { "lat": -25.2222818, "lon": -48.8562271 }
  ],
  "startDate": "2026-03-25",
  "endDate": "2026-03-28",
  "crewIds": [
    "uuid-do-amigo-1",
    "uuid-do-amigo-2"
  ]
}
```

> **Notas:**
> - `crewIds` pode ser `[]` ou omitido para criar uma expedição solo
> - Membros do `crewIds` entram com status `PENDING` (convite pendente)
> - O criador entra automaticamente com status `ACCEPTED`

### Response `201 Created`
```json
{
  "id": "uuid-da-expedição"
}
```

---

## 2. Listar Minhas Expedições

**`GET /expeditions`**

### Query Params

| Param | Tipo | Obrigatório | Padrão | Descrição |
|-------|------|-------------|--------|-----------|
| `status` | `PLANNED` \| `IN_PROGRESS` \| `COMPLETED` \| `CANCELLED` | ❌ | - | Filtra por status da expedição |
| `sort` | `ASC` \| `DESC` | ❌ | `ASC` sem status / `DESC` com status | Direção da ordenação |

> **Ordenação:**
> - **Sem `status`** → ordena por `startDate` (as que começam mais cedo primeiro por padrão)
> - **Com `status`** → ordena por `endDate` (as que terminaram mais recente primeiro por padrão)

### Exemplos
```
GET /expeditions                         → todas, startDate ASC
GET /expeditions?sort=DESC               → todas, startDate DESC
GET /expeditions?status=COMPLETED        → concluídas, endDate DESC
GET /expeditions?status=COMPLETED&sort=ASC → concluídas, endDate ASC
```

### Response `200 OK`
```json
[
  {
    "id": "uuid-da-expedição",
    "title": "Mystic Mountain Trek",
    "trailName": "Trilha do Pico Paraná",
    "distanceMeters": 12500.0,
    "startDate": "2026-03-25",
    "endDate": "2026-03-28",
    "membersCount": 2,
    "members": [
      {
        "inviteId": "uuid-do-convite",
        "id": "uuid-do-usuario",
        "displayName": "João Silva",
        "avatarUrl": "https://exemplo.com/avatar.png",
        "memberStatus": "ACCEPTED"
      },
      {
        "inviteId": "uuid-do-convite-2",
        "id": "uuid-do-amigo",
        "displayName": "Maria Santos",
        "avatarUrl": null,
        "memberStatus": "PENDING"
      }
    ]
  }
]
```

> **Nota:** `memberStatus` pode ser `ACCEPTED` ou `PENDING`. Membros com `REJECTED` ou `REMOVED` não aparecem.

---

## 3. Detalhar Expedição

**`GET /expeditions/{id}`**

> Somente membros com status `ACCEPTED` conseguem acessar.

### Response `200 OK`
```json
{
  "id": "uuid-da-expedição",
  "title": "Mystic Mountain Trek",
  "osmId": 11379327,
  "trailName": "Trilha do Pico Paraná",
  "startDate": "2026-03-25",
  "endDate": "2026-03-28",
  "status": "PLANNED",
  "createdByUserId": "uuid-do-criador",
  "members": [
    {
      "inviteId": "uuid-do-convite",
      "id": "uuid-do-usuario",
      "displayName": "João Silva",
      "avatarUrl": "https://exemplo.com/avatar.png",
      "memberStatus": "ACCEPTED"
    },
    {
      "inviteId": "uuid-do-convite-2",
      "id": "uuid-do-amigo",
      "displayName": "Maria Santos",
      "avatarUrl": null,
      "memberStatus": "PENDING"
    }
  ],
  "geometry": [
    { "lat": -25.2221393, "lon": -48.8563662 },
    { "lat": -25.2222818, "lon": -48.8562271 }
  ],
  "createdAt": "2026-01-10T14:30:00Z"
}
```

> **Nota:** `geometry` contém todos os pontos da trilha — use para renderizar o mapa de navegação.

---

## 4. Editar Expedição

**`PUT /expeditions/{id}`**

> Somente o dono (`createdByUserId`) pode editar.

### Request Body
> Todos os campos são **opcionais**. Envie apenas os que deseja alterar.

```json
{
  "title": "Nome Atualizado",
  "startDate": "2026-04-01",
  "endDate": "2026-04-04",
  "osmId": null,
  "nameTrail": null,
  "tags": null,
  "geometry": null,
  "removedMemberIds": ["uuid-do-membro-a-remover"]
}
```

### Alterar a trilha
Para trocar a trilha, envie `osmId` + `nameTrail` + `tags` + `geometry`:
```json
{
  "title": "Trek Atualizado",
  "startDate": "2026-04-01",
  "endDate": "2026-04-04",
  "osmId": 99999999,
  "nameTrail": "Nova Trilha",
  "tags": { "route": "hiking" },
  "geometry": [
    { "lat": -25.22, "lon": -48.85 }
  ],
  "removedMemberIds": []
}
```

> **Notas:**
> - Se `osmId` for diferente do atual, a nova trilha é salva no banco (idempotente). A trilha antiga permanece.
> - Membros em `removedMemberIds` recebem status `REMOVED` e perdem acesso à expedição.

### Response `204 No Content`

---

## 5. Excluir Expedição

**`DELETE /expeditions/{id}`**

> Somente o dono pode excluir. A exclusão é **soft**: o dono sai da crew e o status da expedição vira `CANCELLED`. Os outros membros continuam vendo a expedição como cancelada.

### Response `204 No Content`

---

## 6. Convidar Amigo para Expedição

**`POST /expeditions/{id}/invites`**

> Apenas membros `ACCEPTED` podem convidar. O convidado deve ser amigo do convidante. Máximo de **3 tentativas** de convite por `(expedição, usuário)`.

### Request Body
```json
{
  "inviteeId": "uuid-do-amigo"
}
```

### Response `204 No Content`

### Erros possíveis
| Código | Motivo |
|--------|--------|
| `403` | Você não é membro da expedição, ou o usuário não é seu amigo |
| `404` | Expedição não encontrada |
| `409` | Usuário já é membro / já tem convite pendente, ou limite de 3 convites atingido |

---

## 7. Responder a Convite de Expedição

**`PATCH /expeditions/invites/{inviteId}/respond`**

> O `inviteId` vem nos campos `inviteId` de cada membro retornado em `/expeditions/invites/pending`. Apenas o próprio convidado pode responder.

### Request Body
```json
{
  "status": "ACCEPTED"
}
```

| Valor de `status` | Efeito |
|-------------------|--------|
| `ACCEPTED` | Usuário entra como membro ativo da expedição |
| `REJECTED` | Convite rejeitado — usuário não entra |

### Response `204 No Content`

### Erros possíveis
| Código | Motivo |
|--------|--------|
| `400` | `status` inválido (deve ser `ACCEPTED` ou `REJECTED`) |
| `403` | O convite não é seu, ou não está mais `PENDING` |
| `404` | Convite não encontrado |

---

## 8. Listar Convites Pendentes de Expedição

**`GET /expeditions/invites/pending`**

> Retorna todos os convites de expedição pendentes para o usuário logado.

### Response `200 OK`
```json
[
  {
    "inviteId": "uuid-do-convite",
    "expeditionId": "uuid-da-expedição",
    "expeditionTitle": "Mystic Mountain Trek",
    "inviterId": "uuid-do-convidante",
    "inviterDisplayName": "João Silva",
    "inviterAvatarUrl": "https://exemplo.com/avatar.png"
  }
]
```

> **Fluxo no app:** Use o `inviteId` para chamar `PATCH /expeditions/invites/{inviteId}/respond`.

---

## Status de Membros (`memberStatus`)

| Status | Descrição |
|--------|-----------|
| `ACCEPTED` | Membro ativo — participa da expedição |
| `PENDING` | Convite enviado, aguardando resposta |
| `REJECTED` | Convite rejeitado — **não aparece nas listagens** |
| `REMOVED` | Removido pelo dono ou auto-exclusão do criador — **não aparece nas listagens** |

---

## Status de Expedição (`status`)

| Status | Descrição |
|--------|-----------|
| `PLANNED` | Expedição planejada, ainda não iniciada |
| `IN_PROGRESS` | Em andamento |
| `COMPLETED` | Concluída |
| `CANCELLED` | Cancelada (dono excluiu) |

