package com.example.spring_boot_mode.utils;

import java.util.regex.Pattern;

/**
 * 聊天室内容清洗工具
 * <p>
 * 主要做两件事：
 * 1. 去除控制字符（包括 NUL、SOH 等不可见字符与大多数 C0/C1 控制字符）。
 * 2. 去除危险的 HTML 标签和事件属性，防止 XSS 与样式注入。
 * <p>
 * 不做整段 HTML 转义——聊天室是纯文本场景，转义后会原样显示给用户，反而体验差。
 * 因此采用"白名单 + 黑名单标签剥离"的策略，仅剥离危险标签，正常文本原样保留。
 * <p>
 * 注意：这是基础防御层，前端仍应使用 v-text（不要用 v-html）渲染。
 *
 * @author mavis
 */
public final class ChatContentSanitizer {

    /** 常见危险 HTML 标签名（不区分大小写） */
    private static final Pattern SCRIPT_TAG = Pattern.compile(
            "<\\s*script\\b[^>]*>.*?<\\s*/\\s*script\\s*>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern SCRIPT_OPEN = Pattern.compile(
            "<\\s*script\\b[^>]*>", Pattern.CASE_INSENSITIVE);
    private static final Pattern IFRAME_TAG = Pattern.compile(
            "<\\s*iframe\\b[^>]*>.*?<\\s*/\\s*iframe\\s*>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern IFRAME_OPEN = Pattern.compile(
            "<\\s*iframe\\b[^>]*>", Pattern.CASE_INSENSITIVE);
    private static final Pattern OBJECT_TAG = Pattern.compile(
            "<\\s*(object|embed|form|input|link|meta|style)\\b[^>]*>.*?<\\s*/\\s*\\1\\s*>",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern OBJECT_OPEN = Pattern.compile(
            "<\\s*(object|embed|form|link|meta|style)\\b[^>]*>", Pattern.CASE_INSENSITIVE);
    private static final Pattern SELF_CLOSING = Pattern.compile(
            "<\\s*(object|embed|form|input|link|meta|style|base|img)\\b[^>]*/?\\s*>",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern ON_EVENT_ATTR = Pattern.compile(
            "\\s+on[a-z]+\\s*=\\s*(\"[^\"]*\"|'[^']*'|[^\\s>]+)", Pattern.CASE_INSENSITIVE);
    private static final Pattern JS_HREF = Pattern.compile(
            "\\s+(href|src|action|formaction|xlink:href)\\s*=\\s*(\"\\s*javascript:[^\"]*\"|'\\s*javascript:[^']*'|\\s*javascript:[^\\s>]+)",
            Pattern.CASE_INSENSITIVE);

    /** 控制字符（保留 \t \n \r 三个常用空白） */
    private static final Pattern CTRL_CHARS = Pattern.compile("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F]");

    /** 多余换行（连续 3 个及以上）压缩成 2 个 */
    private static final Pattern EXCESS_NEWLINES = Pattern.compile("\\n{3,}");

    private ChatContentSanitizer() {
    }

    /**
     * 清洗消息内容
     *
     * @param raw 原始内容
     * @return 清洗后内容；null/空时返回 ""
     */
    public static String sanitize(String raw) {
        if (raw == null) {
            return "";
        }
        String s = raw;

        // 1. 去除控制字符
        s = CTRL_CHARS.matcher(s).replaceAll("");

        // 2. 剥离危险标签（成对 + 自闭合 + 单开标签）
        s = SCRIPT_TAG.matcher(s).replaceAll("");
        s = SCRIPT_OPEN.matcher(s).replaceAll("");
        s = IFRAME_TAG.matcher(s).replaceAll("");
        s = IFRAME_OPEN.matcher(s).replaceAll("");
        s = OBJECT_TAG.matcher(s).replaceAll("");
        s = OBJECT_OPEN.matcher(s).replaceAll("");
        s = SELF_CLOSING.matcher(s).replaceAll("");

        // 3. 去除所有 on* 事件属性
        s = ON_EVENT_ATTR.matcher(s).replaceAll("");

        // 4. 去除 javascript: 伪协议的链接
        s = JS_HREF.matcher(s).replaceAll("");

        // 5. 压缩多余空行
        s = EXCESS_NEWLINES.matcher(s).replaceAll("\n\n");

        return s.trim();
    }

    /**
     * 简单检查是否包含可疑内容（可作为告警/审核输入）
     */
    public static boolean containsSuspicious(String raw) {
        if (raw == null || raw.isEmpty()) return false;
        String lower = raw.toLowerCase();
        return lower.contains("<script") || lower.contains("<iframe") || lower.contains("javascript:")
                || lower.contains("onerror=") || lower.contains("onload=");
    }
}
