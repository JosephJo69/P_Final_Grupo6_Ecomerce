const state = {
    tree: [],
    flatNodes: [],
    selectedNodeId: "",
    activeView: "bfs"
};

const elements = {
    apiStatus: document.querySelector("#apiStatus"),
    nodeCount: document.querySelector("#nodeCount"),
    rootForm: document.querySelector("#rootForm"),
    childForm: document.querySelector("#childForm"),
    parentSelect: document.querySelector("#parentSelect"),
    nodeSelect: document.querySelector("#nodeSelect"),
    treeView: document.querySelector("#treeView"),
    detailsView: document.querySelector("#detailsView"),
    heightValue: document.querySelector("#heightValue"),
    validValue: document.querySelector("#validValue"),
    selectedNodeValue: document.querySelector("#selectedNodeValue"),
    storageLabel: document.querySelector("#storageLabel"),
    refreshButton: document.querySelector("#refreshButton"),
    toast: document.querySelector("#toast")
};

function flattenTree(nodes, depth = 0, result = []) {
    nodes.forEach((node) => {
        result.push({ ...node, depth });
        flattenTree(node.children || [], depth + 1, result);
    });
    return result;
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

function showToast(message, type = "ok") {
    elements.toast.textContent = message;
    elements.toast.className = `toast show ${type === "error" ? "error" : ""}`;
    window.clearTimeout(showToast.timeoutId);
    showToast.timeoutId = window.setTimeout(() => {
        elements.toast.className = "toast";
    }, 2800);
}

async function apiRequest(path, options = {}) {
    const response = await fetch(path, {
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        },
        ...options
    });

    if (!response.ok) {
        let message = `HTTP ${response.status}`;
        try {
            const body = await response.json();
            message = body.error || message;
        } catch (error) {
            message = response.statusText || message;
        }
        throw new Error(message);
    }

    return response.json();
}

function setApiStatus(ok) {
    elements.apiStatus.textContent = ok ? "Conectado" : "Sin conexion";
    elements.apiStatus.className = `status-pill ${ok ? "ok" : "error"}`;
}

function storageDisplayName(storage) {
    const names = {
        memory: "Memoria",
        mongo: "MongoDB",
        postgres: "PostgreSQL"
    };
    return names[storage] || storage || "API REST";
}

function nodeOptionLabel(node) {
    const prefix = "  ".repeat(node.depth || 0);
    return `${prefix}${node.name}`;
}

function renderSelectors() {
    const options = state.flatNodes
        .map((node) => `<option value="${escapeHtml(node.id)}">${escapeHtml(nodeOptionLabel(node))}</option>`)
        .join("");

    elements.parentSelect.innerHTML = options || '<option value="">Sin nodos</option>';
    elements.nodeSelect.innerHTML = options || '<option value="">Sin nodos</option>';
    elements.parentSelect.disabled = state.flatNodes.length === 0;
    elements.nodeSelect.disabled = state.flatNodes.length === 0;

    if (!state.selectedNodeId && state.flatNodes.length > 0) {
        state.selectedNodeId = state.flatNodes[0].id;
    }

    if (state.selectedNodeId) {
        elements.nodeSelect.value = state.selectedNodeId;
    }
}

function renderTreeList(nodes) {
    if (!nodes.length) {
        return '<div class="empty-state">No hay nodos registrados</div>';
    }

    const items = nodes.map((node) => {
        const activeClass = node.id === state.selectedNodeId ? " active" : "";
        const description = node.description ? `<span>${escapeHtml(node.description)}</span>` : "<span>Sin descripcion</span>";
        const children = node.children?.length ? renderTreeList(node.children) : "";

        return `
            <li class="tree-node">
                <button class="node-button${activeClass}" type="button" data-node-id="${escapeHtml(node.id)}">
                    <span class="node-title">${escapeHtml(node.name)}</span>
                    <span class="node-meta">${description}</span>
                    <span class="node-meta">ID: ${escapeHtml(node.id)}</span>
                </button>
                ${children}
            </li>
        `;
    }).join("");

    return `<ul class="tree-list">${items}</ul>`;
}

function renderTree() {
    elements.treeView.innerHTML = renderTreeList(state.tree);
    elements.nodeCount.textContent = `${state.flatNodes.length} ${state.flatNodes.length === 1 ? "nodo" : "nodos"}`;
    const selectedNode = state.flatNodes.find((node) => node.id === state.selectedNodeId);
    elements.selectedNodeValue.textContent = selectedNode ? selectedNode.name : "-";
}

function renderNodeTable(nodes) {
    if (!nodes.length) {
        return '<div class="empty-state">Sin resultados</div>';
    }

    const rows = nodes.map((node, index) => `
        <tr>
            <td>${index + 1}</td>
            <td><strong>${escapeHtml(node.name)}</strong><br><span class="node-meta">${escapeHtml(node.description || "Sin descripcion")}</span></td>
            <td><span class="node-meta">${escapeHtml(node.parentId || "raiz")}</span></td>
        </tr>
    `).join("");

    return `
        <table class="node-table">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Nodo</th>
                    <th>Padre</th>
                </tr>
            </thead>
            <tbody>${rows}</tbody>
        </table>
    `;
}

async function renderDetails() {
    try {
        let data;
        if (state.activeView === "json") {
            elements.detailsView.innerHTML = `<pre class="code-block">${escapeHtml(JSON.stringify(state.tree, null, 2))}</pre>`;
            return;
        }

        if (state.activeView === "path" || state.activeView === "ancestors" || state.activeView === "subtree") {
            if (!state.selectedNodeId) {
                elements.detailsView.innerHTML = '<div class="empty-state">Sin nodo seleccionado</div>';
                return;
            }
            data = await apiRequest(`/tree/${state.selectedNodeId}/${state.activeView}`);
        } else {
            data = await apiRequest(`/tree/${state.activeView}`);
        }

        elements.detailsView.innerHTML = renderNodeTable(Array.isArray(data) ? data : []);
    } catch (error) {
        elements.detailsView.innerHTML = `<div class="empty-state">${escapeHtml(error.message)}</div>`;
    }
}

async function refreshMetrics() {
    try {
        const [height, validation] = await Promise.all([
            apiRequest("/tree/height"),
            apiRequest("/tree/validate")
        ]);

        elements.heightValue.textContent = height.height ?? 0;
        elements.validValue.textContent = validation.valid ? "Valido" : "Revisar";
        elements.validValue.style.color = validation.valid ? "var(--green)" : "var(--amber)";
    } catch (error) {
        elements.heightValue.textContent = "-";
        elements.validValue.textContent = "-";
    }
}

async function refreshStorageLabel() {
    try {
        const config = await apiRequest("/config/storage");
        elements.storageLabel.textContent = `${storageDisplayName(config.storage)} / API REST`;
    } catch (error) {
        elements.storageLabel.textContent = "API REST";
    }
}

async function refreshTree() {
    try {
        const data = await apiRequest("/tree");
        state.tree = Array.isArray(data) ? data : [];
        state.flatNodes = flattenTree(state.tree);

        if (!state.flatNodes.some((node) => node.id === state.selectedNodeId)) {
            state.selectedNodeId = state.flatNodes[0]?.id || "";
        }

        setApiStatus(true);
        renderSelectors();
        renderTree();
        await refreshMetrics();
        await renderDetails();
    } catch (error) {
        setApiStatus(false);
        showToast(error.message, "error");
    }
}

function getFormPayload(form) {
    const data = new FormData(form);
    return {
        name: String(data.get("name") || "").trim(),
        description: String(data.get("description") || "").trim()
    };
}

elements.rootForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    const payload = getFormPayload(elements.rootForm);

    try {
        const created = await apiRequest("/nodes/root", {
            method: "POST",
            body: JSON.stringify(payload)
        });
        state.selectedNodeId = created.id;
        elements.rootForm.reset();
        showToast("Raiz creada");
        await refreshTree();
    } catch (error) {
        showToast(error.message, "error");
    }
});

elements.childForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    const parentId = elements.parentSelect.value;
    const payload = getFormPayload(elements.childForm);

    try {
        const created = await apiRequest(`/nodes/${parentId}/children`, {
            method: "POST",
            body: JSON.stringify(payload)
        });
        state.selectedNodeId = created.id;
        elements.childForm.reset();
        elements.parentSelect.value = parentId;
        showToast("Hijo agregado");
        await refreshTree();
    } catch (error) {
        showToast(error.message, "error");
    }
});

elements.treeView.addEventListener("click", async (event) => {
    const button = event.target.closest("[data-node-id]");
    if (!button) return;
    state.selectedNodeId = button.dataset.nodeId;
    elements.nodeSelect.value = state.selectedNodeId;
    renderTree();
    await renderDetails();
});

elements.nodeSelect.addEventListener("change", async (event) => {
    state.selectedNodeId = event.target.value;
    renderTree();
    await renderDetails();
});

document.querySelectorAll(".tab-button").forEach((button) => {
    button.addEventListener("click", async () => {
        document.querySelectorAll(".tab-button").forEach((item) => item.classList.remove("active"));
        button.classList.add("active");
        state.activeView = button.dataset.view;
        await renderDetails();
    });
});

elements.refreshButton.addEventListener("click", refreshTree);

refreshStorageLabel();
refreshTree();
