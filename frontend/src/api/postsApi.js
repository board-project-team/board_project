import { apiJson, apiFetch } from "./apiFetch";

export function fetchPosts() {
    return apiJson("/api/posts");
}

export function fetchPost(id) {
    return apiJson(`/api/posts/${id}`);
}

export function createPost({ title, content }) {
    return apiJson("/api/posts", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title, content }),
    });
}

export async function updatePost(id, { title, content }) {
    const res = await apiFetch(`/api/posts/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ title, content }),
    });
    if (!res.ok) throw new Error(`Failed to update post (status=${res.status})`);
    try {
        return await res.json();
    } catch {
        return true;
    }
}

export async function deletePost(id) {
    const res = await apiFetch(`/api/posts/${id}`, { method: "DELETE" });
    if (!res.ok) throw new Error(`Failed to delete post (status=${res.status})`);
    return true;
}
