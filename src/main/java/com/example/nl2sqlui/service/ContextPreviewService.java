package com.example.nl2sqlui.service;

import com.example.nl2sqlui.config.AppProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class ContextPreviewService {

    private final JdbcTemplate jdbc;
    private final AppProperties props;

    private String cached;
    private Instant cachedAt;

    private static final Pattern IDENT = Pattern.compile("^[a-zA-Z_][\\w]*$");

    public ContextPreviewService(JdbcTemplate jdbc, AppProperties props) {
        this.jdbc = jdbc;
        this.props = props;
    }

    public Optional<String> getContextText() {
        if (!props.getContext().isEnabled()) return Optional.empty();

        int ttl = props.getContext().getCacheTtlSec();
        if (cached != null && cachedAt != null && cachedAt.plusSeconds(ttl).isAfter(Instant.now())) {
            return Optional.of(cached);
        }

        String schema = buildSchema();
        String enums = buildEnums();
        String out = schema + ((enums == null || enums.trim().isEmpty()) ? "" : "\n\n" + enums);

        cached = out;
        cachedAt = Instant.now();
        return Optional.of(out);
    }

    private String buildSchema() {
        List<String> allowed = props.getContext().getAllowedSchemas();

        String sql;
        Object[] args;

        if (allowed != null && !allowed.isEmpty()) {
            String placeholders = String.join(",", Collections.nCopies(allowed.size(), "?"));
            sql = "SELECT table_schema, table_name, column_name, data_type, ordinal_position\n" +
                  "FROM information_schema.columns\n" +
                  "WHERE table_schema NOT IN ('pg_catalog','information_schema')\n" +
                  "  AND table_schema IN (" + placeholders + ")\n" +
                  "ORDER BY table_schema, table_name, ordinal_position";
            args = allowed.toArray();
        } else {
            sql = "SELECT table_schema, table_name, column_name, data_type, ordinal_position\n" +
                  "FROM information_schema.columns\n" +
                  "WHERE table_schema NOT IN ('pg_catalog','information_schema')\n" +
                  "ORDER BY table_schema, table_name, ordinal_position";
            args = new Object[0];
        }

        List<Map<String, Object>> rows = jdbc.queryForList(sql, args);

        StringBuilder sb = new StringBuilder();
        sb.append("Схема БД (доступные таблицы/колонки):");
        String current = null;

        for (Map<String, Object> r : rows) {
            String key = r.get("table_schema") + "." + r.get("table_name");
            if (!Objects.equals(key, current)) {
                sb.append("\n- ").append(key);
                current = key;
            }
            sb.append("\n    - ").append(r.get("column_name")).append(": ").append(r.get("data_type"));
        }
        if (rows.isEmpty()) sb.append("\n(пусто)");
        return sb.toString();
    }

    private String buildEnums() {
        List<String> cols = props.getContext().getDistinctColumns();
        if (cols == null || cols.isEmpty()) return "";

        int lim = props.getContext().getDistinctLimit();
        StringBuilder sb = new StringBuilder("Справочники значений (top по частоте):");

        for (String item : cols) {
            String[] parts = item.split("\\.");
            if (parts.length != 3) continue;

            String sch = parts[0], tbl = parts[1], col = parts[2];
            if (!safeIdent(sch) || !safeIdent(tbl) || !safeIdent(col)) continue;

            String sql = "SELECT \"" + col + "\" AS value, COUNT(*) AS cnt " +
                    "FROM \"" + sch + "\".\"" + tbl + "\" " +
                    "GROUP BY \"" + col + "\" " +
                    "ORDER BY cnt DESC NULLS LAST " +
                    "LIMIT ?";

            List<Map<String, Object>> rows;
            try {
                rows = jdbc.queryForList(sql, lim);
            } catch (Exception e) {
                continue;
            }
            if (rows.isEmpty()) continue;

            sb.append("\n- ").append(item).append(": ");
            List<String> partsOut = new ArrayList<>();

            for (Map<String, Object> r : rows) {
                Object v = r.get("value");
                Object c = r.get("cnt");
                String vStr = (v == null) ? "NULL" : String.valueOf(v).replace("\n", " ").trim();
                if (vStr.length() > 40) vStr = vStr.substring(0, 37) + "...";
                partsOut.add(vStr + "(" + c + ")");
            }
            sb.append(String.join(", ", partsOut));
        }

        return sb.toString();
    }

    private boolean safeIdent(String s) {
        return s != null && IDENT.matcher(s).matches();
    }
}
