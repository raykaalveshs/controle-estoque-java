package view;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.DAO;

public class Janela extends JFrame {
	DAO dao = new DAO();
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;
	private FileInputStream fis;
	private int tamanho;
	private boolean fotoCarregada = false;
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtCodigo;
	private JTextField txtDepartamento;
	private JTextField txtQuantidade;
	private JTextField txtDescricao;
	private JTextField txtPreco;
	private JLabel lblFoto;
	private JButton btnBuscar;
	private JButton btnReset;
	private JButton btnPdf;
	private JList<String> lista;
	private JScrollPane scroll;
	private JButton btnEditar;
	private JButton btnExcluir;
	private JButton btnAdicionar;
	private JButton btnCarregar;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Janela frame = new Janela();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Janela() {
		setResizable(false);
		setTitle("Zyro");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Janela.class.getResource("/img/logo.png")));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 873, 557);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(217, 217, 217));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLayeredPane layeredPane = 	new JLayeredPane();
		layeredPane.setBounds(-10, -39, 857, 557);
		contentPane.add(layeredPane);

		JPanel panel1 = new JPanel();
		panel1.setBackground(new Color(58, 7, 22));
		panel1.setBounds(0, 26, 857, 129);

		JLabel lblLogo = new JLabel("");
		lblLogo.setIcon(new ImageIcon(Janela.class.getResource("/img/logo.png")));
		lblLogo.setBounds(289, 0, 290, 209);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(19, 17, 74));
		panel_1.setBounds(0, 62, 857, 69);
		layeredPane.add(panel_1);

		layeredPane.add(panel1, Integer.valueOf(0));

		JPanel panel = new JPanel();
		panel1.add(panel);
		panel.setBackground(new Color(19, 17, 54));
		layeredPane.add(lblLogo, Integer.valueOf(2));

		JLabel lblNewLabel = new JLabel("Cadastro de Produtos");
		lblNewLabel.setForeground(new Color(136, 15, 46));
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 28));
		lblNewLabel.setBounds(188, 178, 315, 39);
		layeredPane.add(lblNewLabel);
		
		scroll = new JScrollPane();
		scroll.setBounds(188, 261, 157, 75);
		scroll.setVisible(false);
		layeredPane.add(scroll);
		
				lista = new JList<>();
				scroll.setViewportView(lista);
				
						lista.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent e) {
								selecionarLista();
							}
						});

		btnPdf = new JButton("");
		btnPdf.setBorder(new LineBorder(new Color(128, 0, 0), 2));
		btnPdf.setBackground(SystemColor.control);
		btnPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gerarPdf();
			}
		});

		JLabel lblDescricao = new JLabel("Sessão");
		lblDescricao.setForeground(new Color(136, 15, 46));
		lblDescricao.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblDescricao.setBounds(122, 389, 61, 25);
		layeredPane.add(lblDescricao);

		JLabel lblNewLabel_1_1 = new JLabel("Código da Roupa");
		lblNewLabel_1_1.setForeground(new Color(136, 15, 46));
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_1.setBounds(45, 232, 136, 25);
		layeredPane.add(lblNewLabel_1_1);	

		JLabel lblNewLabel_1_2 = new JLabel("Departamento");
		lblNewLabel_1_2.setForeground(new Color(136, 15, 46));
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_2.setBounds(66, 284, 111, 25);
		layeredPane.add(lblNewLabel_1_2);

		txtCodigo = new JTextField();
		txtCodigo.setBounds(187, 235, 157, 25);
		layeredPane.add(txtCodigo);
		txtCodigo.setColumns(10);

		txtCodigo.addKeyListener(new KeyAdapter() {
		    @Override
		    public void keyReleased(KeyEvent e) {
		        listar();
		    }
		});	

		txtDepartamento = new JTextField();
		txtDepartamento.setColumns(10);
		txtDepartamento.setBounds(187, 287, 270, 25);
		layeredPane.add(txtDepartamento);
		


		txtQuantidade = new JTextField();
		txtQuantidade.setColumns(10);
		txtQuantidade.setBounds(187, 338, 270, 25);
		layeredPane.add(txtQuantidade);

		btnCarregar = new JButton("Carregar Foto");
		btnCarregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carregarFoto();
			}

		});
		btnCarregar.setForeground(new Color(255, 255, 255));
		btnCarregar.setBorder(null);
		btnCarregar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCarregar.setBackground(new Color(128, 0, 0));
		btnCarregar.setBounds(596, 451, 127, 31);
		layeredPane.add(btnCarregar);

		txtDescricao = new JTextField();
		txtDescricao.setColumns(10);
		txtDescricao.setBounds(187, 389, 270, 25);
		layeredPane.add(txtDescricao);

		txtPreco = new JTextField();
		txtPreco.setColumns(10);	
		txtPreco.setBounds(187, 441, 270, 25);
		layeredPane.add(txtPreco);

		JLabel txtTexto = new JLabel("Preço");
		txtTexto.setForeground(new Color(136, 15, 46));
		txtTexto.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtTexto.setBounds(136, 441, 43, 25);
		layeredPane.add(txtTexto);

		JLabel lblNewLabel_1_2_1_1 = new JLabel("Quantidade");
		lblNewLabel_1_2_1_1.setForeground(new Color(136, 15, 46));
		lblNewLabel_1_2_1_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel_1_2_1_1.setBounds(88, 338, 95, 25);
		layeredPane.add(lblNewLabel_1_2_1_1);

		btnAdicionar = new JButton("");
		btnAdicionar.setBackground(SystemColor.control);
		btnAdicionar.setIcon(new ImageIcon(Janela.class.getResource("/img/create.png")));
		btnAdicionar.setForeground(SystemColor.window);
		btnAdicionar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnAdicionar.setBorder(new LineBorder(new Color(128, 0, 0), 2));
		btnAdicionar.setBounds(207, 477, 50, 31);
		layeredPane.add(btnAdicionar);
		btnAdicionar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionar();
			}
		});

		btnAdicionar.setToolTipText("Adicionar");

		lblFoto = new JLabel("");
		lblFoto.setBounds(527, 193, 256, 238);
		layeredPane.add(lblFoto);
		lblFoto.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		lblFoto.setIcon(new ImageIcon(Janela.class.getResource("/img/roupa.png")));

		btnBuscar = new JButton("");
		btnBuscar.setBackground(SystemColor.control);
		btnBuscar.setIcon(new ImageIcon(Janela.class.getResource("/img/search.png")));
		btnBuscar.setBounds(354, 232, 43, 29);
		layeredPane.add(btnBuscar);
		btnBuscar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnBuscar.setBorder(new LineBorder(new Color(128, 0, 0), 2));
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BuscarCodigo();
			}
		});
		
			

		btnEditar = new JButton("");
		btnEditar.setBackground(SystemColor.control);
		btnEditar.setBorder(new LineBorder(new Color(128, 0, 0), 2));
		btnEditar.setIcon(new ImageIcon(Janela.class.getResource("/img/upload.png")));
		btnEditar.setBounds(267, 477, 50, 31);
		layeredPane.add(btnEditar);

		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editar();
			}
		});

		btnExcluir = new JButton("");
		btnExcluir.setBackground(SystemColor.control);
		btnExcluir.setBorder(new LineBorder(new Color(128, 0, 0), 2));
		btnExcluir.setIcon(new ImageIcon(Janela.class.getResource("/img/delete.png")));
		btnExcluir.setBounds(327, 477, 50, 31);
		layeredPane.add(btnExcluir);

		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				excluir();
			}

		});

		btnPdf.setIcon(new ImageIcon(Janela.class.getResource("/img/pdf.png")));
		btnPdf.setToolTipText("Gerar relatório");
		btnPdf.setBounds(387, 477, 50, 31);
		layeredPane.add(btnPdf);

		btnBuscar.setForeground(SystemColor.window);

		btnReset = new JButton("Resetar");
		btnReset.setIcon(null);
		btnReset.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnReset.setForeground(SystemColor.window);
		btnReset.setBorder(null);
		btnReset.setBackground(new Color(128, 0, 0));
		btnReset.setBounds(407, 231, 50, 31);
		layeredPane.add(btnReset);
		btnReset.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				reset();
			}

		});
		btnReset.setToolTipText("Limpar Campos");
		btnEditar.setEnabled(false);
		btnExcluir.setEnabled(false);
		btnCarregar.setEnabled(true);
		btnAdicionar.setEnabled(false);

	}

	private void carregarFoto() {
		fotoCarregada = true;
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Selecionar Arquivo");
		jfc.setFileFilter(new FileNameExtensionFilter("Arquivo de imagens(*.PNG, *.JPEG, *.JPG", "png", "jpeg", "jpg"));
		int resultado = jfc.showOpenDialog(this);
		if (resultado == JFileChooser.APPROVE_OPTION) {
			try {
				fis = new FileInputStream(jfc.getSelectedFile());
				tamanho = (int) jfc.getSelectedFile().length();
				Image foto = ImageIO.read(jfc.getSelectedFile()).getScaledInstance(lblFoto.getWidth(),
						lblFoto.getHeight(), Image.SCALE_SMOOTH);
				lblFoto.setIcon(new ImageIcon(foto));
				lblFoto.updateUI();

			} catch (Exception e) {
				System.out.println(e);
			}
		}

	}

	private void adicionar() {

		if (txtCodigo.getText().isEmpty() || txtDepartamento.getText().isEmpty() || txtQuantidade.getText().isEmpty()
				|| txtDescricao.getText().isEmpty() || txtPreco.getText().isEmpty()) {

			JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
			return;
		}

		String insert = "insert into itens (codigo, departamento, quantidade, descricao, preco, foto) values(?,?,?,?,?,?)";

		try {
			con = dao.conectar();
			pst = con.prepareStatement(insert);

			pst.setString(1, txtCodigo.getText());
			pst.setString(2, txtDepartamento.getText());
			pst.setString(3, txtQuantidade.getText());
			pst.setString(4, txtDescricao.getText());
			pst.setString(5, txtPreco.getText());

			if (fis == null) {
				pst.setNull(6, java.sql.Types.BLOB);
			} else {
				pst.setBlob(6, fis, tamanho);
			}

			int confirma = pst.executeUpdate();

			if (confirma == 1) {
				JOptionPane.showMessageDialog(null, "Envio bem sucedido!");
				reset();
			} else {
				JOptionPane.showMessageDialog(null, "Erro no envio!");
			}

			if (rs != null) rs.close();
			if (pst != null) pst.close();
			if (con != null) con.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void BuscarCodigo() {
		try {
			String sql = "select * from itens where codigo=?";

			con = dao.conectar();
			pst = con.prepareStatement(sql);
			pst.setString(1, txtCodigo.getText());

			rs = pst.executeQuery();

			if (rs.next()) {
				btnEditar.setEnabled(true);
				btnExcluir.setEnabled(true);
				btnAdicionar.setEnabled(false);
				txtCodigo.setEditable(false);
				txtDepartamento.setText(rs.getString(2));
				txtQuantidade.setText(rs.getString(3));
				txtDescricao.setText(rs.getString(4));
				txtPreco.setText(rs.getString(5));

				Blob blob = rs.getBlob(6);

				if (blob != null) {
					byte[] img = blob.getBytes(1, (int) blob.length());

					if (img != null && img.length > 0) {
						ImageIcon imagem = new ImageIcon(img);

						Image foto = imagem.getImage().getScaledInstance(lblFoto.getWidth(), lblFoto.getHeight(),
								Image.SCALE_SMOOTH);

						lblFoto.setIcon(new ImageIcon(foto));
					} else {
						lblFoto.setIcon(new ImageIcon(getClass().getResource("/img/roupa.png")));
					}
				} else {
					lblFoto.setIcon(new ImageIcon(getClass().getResource("/img/roupa.png")));
				}

			} else {
				JOptionPane.showMessageDialog(null, "Produto não encontrado!");
			}

			if (rs != null) rs.close();
			if (pst != null) pst.close();
			if (con != null) con.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void editar() {

	    String update = "update itens set departamento=?, quantidade=?, descricao=?, preco=?, foto=? where codigo=?";

	    try {
	        con = dao.conectar();
	        pst = con.prepareStatement(update);

	        pst.setString(1, txtDepartamento.getText());
	        pst.setString(2, txtQuantidade.getText());
	        pst.setString(3, txtDescricao.getText());
	        pst.setString(4, txtPreco.getText());

	        if (fotoCarregada) {
	            pst.setBlob(5, fis, tamanho);
	        } else {
	            pst.setNull(5, java.sql.Types.BLOB); 
	        }

	        pst.setString(6, txtCodigo.getText());

	        int resultado = pst.executeUpdate();

	        if (resultado == 1) {
	            JOptionPane.showMessageDialog(null, "Atualizado com sucesso!");
	            reset();
	        } else {
	            JOptionPane.showMessageDialog(null, "Nada foi atualizado!");
	        }

	        if (rs != null) rs.close();
	        if (pst != null) pst.close();
	        if (con != null) con.close();

	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(null, "Erro ao atualizar: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	private void excluir() {

		int confirma = JOptionPane.showConfirmDialog(null, "Confirma a exclusão?", "Atenção",
				JOptionPane.YES_NO_OPTION);

		if (confirma == JOptionPane.YES_OPTION) {

			String delete = "delete from itens where codigo=?";

			try {
				con = dao.conectar();
				pst = con.prepareStatement(delete);
				pst.setString(1, txtCodigo.getText());

				pst.executeUpdate();

				JOptionPane.showMessageDialog(null, "Excluído com sucesso!");
				reset();

				if (rs != null) rs.close();
				if (pst != null) pst.close();
				if (con != null) con.close();

			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	private void gerarPdf() {
		Document document = new Document();

		try {
			PdfWriter.getInstance(document, new FileOutputStream("relatorio.pdf"));
			document.open();

			Date data = new Date();
			DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL);
			document.add(new Paragraph(formatador.format(data)));

			document.add(new Paragraph(" "));
			document.add(new Paragraph("RELATÓRIO DE PRODUTOS"));
			document.add(new Paragraph(" "));

			PdfPTable tabela = new PdfPTable(6);

			tabela.addCell("Código");
			tabela.addCell("Departamento");
			tabela.addCell("Quantidade");
			tabela.addCell("Descrição");
			tabela.addCell("Preço");
			tabela.addCell("Foto");

			String sql = "select * from itens order by codigo asc";

			con = dao.conectar();
			pst = con.prepareStatement(sql);
			rs = pst.executeQuery();

			while (rs.next()) {

				tabela.addCell(rs.getString("codigo"));
				tabela.addCell(rs.getString("departamento"));
				tabela.addCell(rs.getString("quantidade"));
				tabela.addCell(rs.getString("descricao"));
				tabela.addCell(rs.getString("preco"));

				Blob blob = rs.getBlob("foto");

				if (blob != null) {
				    try {
				        byte[] img = blob.getBytes(1, (int) blob.length());

				        if (img != null && img.length > 0) {
				            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(img);
				            image.scaleToFit(80, 80);
				            tabela.addCell(image);
				        } else {
				            tabela.addCell("Sem imagem");
				        }

				    } catch (Exception e) {
				        tabela.addCell("Imagem inválida");
				    }
				} else {
				    tabela.addCell("Sem imagem");
				}
			}

			document.add(tabela);
			if (rs != null) rs.close();
			if (pst != null) pst.close();
			if (con != null) con.close();
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao gerar PDF: " + e.getMessage());
			e.printStackTrace();
		} finally {
			document.close();
		}

		try {
			Desktop.getDesktop().open(new File("relatorio.pdf"));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "PDF gerado, mas não foi possível abrir.");
		}
	}

	private void listar() {
	    if (txtCodigo.getText().isEmpty()) {
	        scroll.setVisible(false);
	        return;
	    }

	    DefaultListModel<String> modelo = new DefaultListModel<>();
	    lista.setModel(modelo);

	    String sql = "select codigo from itens where codigo like ? order by codigo";
	    

	    try {
	        con = dao.conectar();
	        pst = con.prepareStatement(sql);
	        pst.setString(1, txtCodigo.getText() + "%");

	        rs = pst.executeQuery();

	        while (rs.next()) {
	            modelo.addElement(rs.getString("codigo"));
	        }

	        scroll.setVisible(modelo.size() > 0);	

	    } catch (Exception e) {
	        System.out.println(e);
	    }
	}

	private void selecionarLista() {
	    String codigoSelecionado = lista.getSelectedValue();

	    if (codigoSelecionado != null) {

	        String sql = "select * from itens where codigo=?";

	        try {
	            con = dao.conectar();
	            pst = con.prepareStatement(sql);
	            pst.setString(1, codigoSelecionado);

	            rs = pst.executeQuery();

	            if (rs.next()) {
	            	btnEditar.setEnabled(true);
	            	btnExcluir.setEnabled(true);
	            	btnAdicionar.setEnabled(false);
	            	txtCodigo.setEditable(false);
	                txtCodigo.setText(rs.getString("codigo"));
	                txtDepartamento.setText(rs.getString("departamento"));
	                txtQuantidade.setText(rs.getString("quantidade"));
	                txtDescricao.setText(rs.getString("descricao"));
	                txtPreco.setText(rs.getString("preco"));
	                
	                Blob blob = rs.getBlob("foto");

	                if (blob != null) {
	                    byte[] img = blob.getBytes(1, (int) blob.length());
	                    ImageIcon imagem = new ImageIcon(img);

	                    Image foto = imagem.getImage().getScaledInstance(
	                        lblFoto.getWidth(),
	                        lblFoto.getHeight(),
	                        Image.SCALE_SMOOTH
	                    );

	                    lblFoto.setIcon(new ImageIcon(foto));
	                } else {
	                    lblFoto.setIcon(new ImageIcon(getClass().getResource("/img/roupa.png")));
	                }
	            }

	            scroll.setVisible(false);
	            if (rs != null) rs.close();
	            if (pst != null) pst.close();
	            if (con != null) con.close();

	        } catch (Exception e) {
	            System.out.println(e);
	        }
	    }
	}
	
	private void reset() {
	    txtCodigo.setText(null);
	    txtDepartamento.setText(null);
	    txtQuantidade.setText(null);
	    txtDescricao.setText(null);
	    txtPreco.setText(null);

	    lblFoto.setIcon(new ImageIcon(Janela.class.getResource("/img/roupa.png")));

	    fis = null;
	    tamanho = 0;
	    fotoCarregada = false;

	    scroll.setVisible(false);

	    txtCodigo.setEditable(true);

	    btnAdicionar.setEnabled(true);
	    btnEditar.setEnabled(false);
	    btnExcluir.setEnabled(false);

	    txtCodigo.requestFocus();
	}

}
