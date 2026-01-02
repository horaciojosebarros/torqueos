package br.com.torqueos.util;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;

import br.com.torqueos.model.OrdemServico;
import br.com.torqueos.model.PecaOS;
import br.com.torqueos.model.ServicoOS;

public class PdfOrdemServicoGenerator {

  public static byte[] gerar(OrdemServico os,
                             List<ServicoOS> servicos,
                             List<PecaOS> pecas,
                             String empresaNome,
                             String empresaCnpj,
                             String assinaturaLinha1,
                             String assinaturaLinha2) {
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      Document doc = new Document(PageSize.A4, 36, 36, 28, 36);
      PdfWriter.getInstance(doc, out);
      doc.open();

      // Fontes
      Font title = new Font(Font.HELVETICA, 16, Font.BOLD);
      Font h = new Font(Font.HELVETICA, 11, Font.BOLD);
      Font normal = new Font(Font.HELVETICA, 10, Font.NORMAL);
      Font small = new Font(Font.HELVETICA, 9, Font.NORMAL);

      // =========================
      // CABEÇALHO
      // =========================
      PdfPTable header = new PdfPTable(new float[] { 3.5f, 2f });
      header.setWidthPercentage(100);

      // Coluna esquerda: Empresa
      PdfPCell left = new PdfPCell();
      left.setBorder(Rectangle.NO_BORDER);

      Paragraph pEmpresa = new Paragraph(safe(empresaNome), new Font(Font.HELVETICA, 13, Font.BOLD));
      left.addElement(pEmpresa);

      if (empresaCnpj != null && !empresaCnpj.trim().isEmpty()) {
        left.addElement(new Paragraph("CNPJ: " + empresaCnpj, small));
      }

      // assinatura
      if (assinaturaLinha1 != null && !assinaturaLinha1.trim().isEmpty()) {
        left.addElement(new Paragraph(assinaturaLinha1, small));
      }
      if (assinaturaLinha2 != null && !assinaturaLinha2.trim().isEmpty()) {
        left.addElement(new Paragraph(assinaturaLinha2, small));
      }

      header.addCell(left);

      // Coluna direita: OS e geração
      PdfPCell right = new PdfPCell();
      right.setBorder(Rectangle.NO_BORDER);
      right.setHorizontalAlignment(Element.ALIGN_RIGHT);

      right.addElement(new Paragraph("ORDEM DE SERVIÇO", title));

      String osNum = "OS #" + safe(os.getIdOs());
      right.addElement(new Paragraph(osNum, h));

      String geradoEm = "Gerado em: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
      right.addElement(new Paragraph(geradoEm, small));

      header.addCell(right);

      doc.add(header);

      // linha separadora
      LineSeparator sep = new LineSeparator();
      sep.setLineWidth(1f);
      doc.add(new Chunk(sep));
      doc.add(Chunk.NEWLINE);

      // =========================
      // DADOS PRINCIPAIS
      // =========================
      PdfPTable info = new PdfPTable(new float[] { 1.3f, 3.7f });
      info.setWidthPercentage(100);

      info.addCell(labelCell("Cliente"));
      info.addCell(valueCell(os.getCliente() != null ? os.getCliente().getNome() : "-", normal));

      info.addCell(labelCell("Veículo"));
      String veic = "-";
      if (os.getVeiculo() != null) {
        veic = safe(os.getVeiculo().getPlaca()) + " - " + safe(os.getVeiculo().getModelo());
      }
      info.addCell(valueCell(veic, normal));

      info.addCell(labelCell("Status"));
      info.addCell(valueCell(safe(os.getStatus()), normal));

      info.addCell(labelCell("Observações"));
      info.addCell(valueCell(safe(os.getObservacoes()), normal));

      doc.add(info);
      doc.add(Chunk.NEWLINE);

      // =========================
      // SERVIÇOS
      // =========================
      doc.add(new Paragraph("Serviços", h));
      doc.add(Chunk.NEWLINE);

      PdfPTable tServ = new PdfPTable(new float[] { 4f, 1.5f });
      tServ.setWidthPercentage(100);
      tServ.addCell(th("Descrição"));
      tServ.addCell(thRight("Valor (R$)"));

      BigDecimal totalServ = BigDecimal.ZERO;

      if (servicos != null && !servicos.isEmpty()) {
        for (ServicoOS s : servicos) {
          tServ.addCell(valueCell(safe(s.getDescricao()), normal));
          BigDecimal v = (s.getValor() != null ? s.getValor() : BigDecimal.ZERO);
          totalServ = totalServ.add(v);
          tServ.addCell(valueCell(formatMoney(v), normal, Element.ALIGN_RIGHT));
        }
      } else {
        PdfPCell c = valueCell("Nenhum serviço", normal);
        c.setColspan(2);
        tServ.addCell(c);
      }

      doc.add(tServ);
      doc.add(Chunk.NEWLINE);

      // =========================
      // PEÇAS
      // =========================
      doc.add(new Paragraph("Peças", h));
      doc.add(Chunk.NEWLINE);

      PdfPTable tPecas = new PdfPTable(new float[] { 3.6f, 0.8f, 1.4f, 1.4f });
      tPecas.setWidthPercentage(100);
      tPecas.addCell(th("Descrição"));
      tPecas.addCell(thRight("Qtd"));
      tPecas.addCell(thRight("Unit (R$)"));
      tPecas.addCell(thRight("Subtotal (R$)"));

      BigDecimal totalPecas = BigDecimal.ZERO;

      if (pecas != null && !pecas.isEmpty()) {
        for (PecaOS p : pecas) {
          int qtd = (p.getQuantidade() != null ? p.getQuantidade() : 0);
          BigDecimal unit = (p.getValorUnitario() != null ? p.getValorUnitario() : BigDecimal.ZERO);
          BigDecimal sub = unit.multiply(new BigDecimal(qtd));
          totalPecas = totalPecas.add(sub);

          tPecas.addCell(valueCell(safe(p.getDescricao()), normal));
          tPecas.addCell(valueCell(String.valueOf(qtd), normal, Element.ALIGN_RIGHT));
          tPecas.addCell(valueCell(formatMoney(unit), normal, Element.ALIGN_RIGHT));
          tPecas.addCell(valueCell(formatMoney(sub), normal, Element.ALIGN_RIGHT));
        }
      } else {
        PdfPCell c = valueCell("Nenhuma peça", normal);
        c.setColspan(4);
        tPecas.addCell(c);
      }

      doc.add(tPecas);
      doc.add(Chunk.NEWLINE);

      // =========================
      // TOTAIS
      // =========================
      BigDecimal total = totalServ.add(totalPecas);

      PdfPTable tot = new PdfPTable(new float[] { 2.2f, 1.3f });
      tot.setWidthPercentage(35);
      tot.setHorizontalAlignment(Element.ALIGN_RIGHT);

      tot.addCell(labelCell("Total serviços"));
      tot.addCell(valueCell(formatMoney(totalServ), normal, Element.ALIGN_RIGHT));

      tot.addCell(labelCell("Total peças"));
      tot.addCell(valueCell(formatMoney(totalPecas), normal, Element.ALIGN_RIGHT));

      PdfPCell totalLabel = labelCell("Total geral");
      totalLabel.setBackgroundColor(new java.awt.Color(240, 240, 240));
      tot.addCell(totalLabel);

      PdfPCell totalValue = valueCell(formatMoney(total), new Font(Font.HELVETICA, 10, Font.BOLD), Element.ALIGN_RIGHT);
      totalValue.setBackgroundColor(new java.awt.Color(240, 240, 240));
      tot.addCell(totalValue);

      doc.add(tot);

      // Rodapé simples
      doc.add(Chunk.NEWLINE);
      Paragraph footer = new Paragraph("Documento gerado pelo TorqueOS", small);
      footer.setAlignment(Element.ALIGN_CENTER);
      footer.getFont().setColor(new java.awt.Color(120, 120, 120));
      doc.add(footer);

      doc.close();
      return out.toByteArray();
    } catch (Exception e) {
      throw new RuntimeException("Erro ao gerar PDF da OS", e);
    }
  }

  // ===== helpers layout =====

  private static PdfPCell th(String text) {
    PdfPCell c = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 10, Font.BOLD)));
    c.setPadding(6);
    c.setBackgroundColor(new java.awt.Color(245, 245, 245));
    return c;
  }

  private static PdfPCell thRight(String text) {
    PdfPCell c = th(text);
    c.setHorizontalAlignment(Element.ALIGN_RIGHT);
    return c;
  }

  private static PdfPCell labelCell(String text) {
    PdfPCell c = new PdfPCell(new Phrase(text, new Font(Font.HELVETICA, 10, Font.BOLD)));
    c.setPadding(6);
    c.setBackgroundColor(new java.awt.Color(245, 245, 245));
    return c;
  }

  private static PdfPCell valueCell(String text, Font font) {
    return valueCell(text, font, Element.ALIGN_LEFT);
  }

  private static PdfPCell valueCell(String text, Font font, int align) {
    PdfPCell c = new PdfPCell(new Phrase(text != null ? text : "", font));
    c.setPadding(6);
    c.setHorizontalAlignment(align);
    return c;
  }

  private static String safe(Object o) {
    return o == null ? "" : String.valueOf(o);
  }

  private static String formatMoney(BigDecimal v) {
    if (v == null) return "0.00";
    // mantém simples e compatível (sem locale)
    return v.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
  }
}
