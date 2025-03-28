package tema8Entrega;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JScrollPane;
import java.sql.*;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Tema8Entrega {

	private JFrame frmSupermercado;
	private JTable tablaProductos;
	private JTextField textFieldDesc;
	private JTextField textFieldVenta;
	private JTextField textFieldAlmacen;
	private JTextField textFieldCaducidad;
	
	boolean comprobarExpReg(String cadena, String patron) {
		Pattern pat=Pattern.compile(patron);
		Matcher mat=pat.matcher(cadena);
		
		if (mat.matches()) {
			return true;
		}else {
			return false;
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tema8Entrega window = new Tema8Entrega();
					window.frmSupermercado.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Tema8Entrega() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSupermercado = new JFrame();
		frmSupermercado.setTitle("Supermercado");
		frmSupermercado.setBounds(100, 100, 956, 563);
		frmSupermercado.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSupermercado.getContentPane().setLayout(null);
		
		DefaultTableModel productos = new DefaultTableModel();
		productos.addColumn("Código");
		productos.addColumn("Nombre y descripción");
		productos.addColumn("Unidades a la venta");
		productos.addColumn("Unidades en el almacén");
		productos.addColumn("Unidades totales");
		productos.addColumn("Fecha de caducidad");
		
		tablaProductos = new JTable(productos);
		tablaProductos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int indice= tablaProductos.getSelectedRow();
				TableModel modelo=tablaProductos.getModel();
				
				textFieldDesc.setText(String.valueOf(modelo.getValueAt(indice, 1)));
				textFieldVenta.setText(String.valueOf(modelo.getValueAt(indice, 2)));
				textFieldAlmacen.setText(String.valueOf(modelo.getValueAt(indice, 3)));
				textFieldCaducidad.setText(String.valueOf(modelo.getValueAt(indice, 5)));
			}
		});
		tablaProductos.setBounds(58, 28, 858, 239);
		frmSupermercado.getContentPane().add(tablaProductos);
		
		JScrollPane scrollPane = new JScrollPane(tablaProductos);
		scrollPane.setBounds(58, 28, 858, 239);
		frmSupermercado.getContentPane().add(scrollPane);
		
		JLabel lblFechaDeCaducidad = new JLabel("Fecha de caducidad");
		lblFechaDeCaducidad.setBounds(58, 411, 156, 15);
		frmSupermercado.getContentPane().add(lblFechaDeCaducidad);
		
		textFieldCaducidad = new JTextField();
		textFieldCaducidad.setBounds(248, 408, 114, 19);
		frmSupermercado.getContentPane().add(textFieldCaducidad);
		textFieldCaducidad.setColumns(10);
		
		JLabel lblDescripcin = new JLabel("Descripción");
		lblDescripcin.setBounds(58, 297, 93, 15);
		frmSupermercado.getContentPane().add(lblDescripcin);
		
		textFieldDesc = new JTextField();
		textFieldDesc.setBounds(169, 295, 522, 17);
		frmSupermercado.getContentPane().add(textFieldDesc);
		textFieldDesc.setColumns(10);
		
		JLabel lblUnidadesALa = new JLabel("Unidades a la venta");
		lblUnidadesALa.setBounds(58, 338, 156, 15);
		frmSupermercado.getContentPane().add(lblUnidadesALa);
		
		JLabel lblUnidadesEnAlmacn = new JLabel("Unidades en almacén");
		lblUnidadesEnAlmacn.setBounds(58, 379, 156, 15);
		frmSupermercado.getContentPane().add(lblUnidadesEnAlmacn);
		
		textFieldVenta = new JTextField();
		textFieldVenta.setBounds(245, 336, 117, 17);
		frmSupermercado.getContentPane().add(textFieldVenta);
		textFieldVenta.setColumns(10);
		
		textFieldAlmacen = new JTextField();
		textFieldAlmacen.setBounds(248, 377, 114, 19);
		frmSupermercado.getContentPane().add(textFieldAlmacen);
		textFieldAlmacen.setColumns(10);
		
		try {
			Connection con = ConnectionSingleton.getConnection();
			Statement muestra = con.createStatement();
			ResultSet prod = muestra.executeQuery("SELECT * FROM productos");
			productos.setRowCount(0);
			while (prod.next()) {
				Object[] row = new Object [6];
				row[0] = prod.getInt("codigo");
				row[1] = prod.getString("descripcion");
				row[2]=prod.getInt("unidades_venta");
				row[3]=prod.getInt("unidades_almacen");
				row[4]=prod.getInt("unidades_total");
				row[5]=prod.getObject("fecha_caducidad",LocalDate.class);
				productos.addRow(row);
			}
		
		JButton btnAgregar = new JButton("AÑADIR");
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				if (textFieldDesc.getText().length()==0) {
					JOptionPane.showMessageDialog(frmSupermercado, "La descipción está vacía","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (textFieldVenta.getText().length()==0){
					JOptionPane.showMessageDialog(frmSupermercado, "Las unidades en venta están vacías","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (textFieldAlmacen.getText().length()==0){
					JOptionPane.showMessageDialog(frmSupermercado, "Las unidades en almacén están vacías","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (textFieldCaducidad.getText().length()==0){
					JOptionPane.showMessageDialog(frmSupermercado, "La fecha de caducidad está vacía","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (!comprobarExpReg(textFieldDesc.getText(),"^\\D+$")){
					JOptionPane.showMessageDialog(frmSupermercado, "El nombre debe ser un conjunto de cadenas alfabéticas","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (!comprobarExpReg(textFieldVenta.getText(),"^\\d{1,2}$")){
					JOptionPane.showMessageDialog(frmSupermercado, "Las unidades de venta deben ser un número entero de dos cifras como mucho","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (!comprobarExpReg(textFieldAlmacen.getText(),"^\\d{1,2}$")){
					JOptionPane.showMessageDialog(frmSupermercado, "Las unidades de almacén deben ser un número entero de dos cifras como mucho","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (!comprobarExpReg(textFieldCaducidad.getText(),"^\\d{4}\\-\\d{2}\\-\\d{2}$")){
					JOptionPane.showMessageDialog(frmSupermercado, "La fecha de caducidad debe tener formato aaaa-mm-dd","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else  if ((LocalDate.parse(textFieldCaducidad.getText()).isBefore(LocalDate.now()))){
					JOptionPane.showMessageDialog(frmSupermercado, "La fecha de caducidad debe ser posterior a la fecha actual","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else {
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement insertProd= con.prepareStatement("INSERT INTO productos(descripcion,unidades_venta,unidades_almacen, unidades_total, fecha_caducidad) VALUES (?,?,?,?,?)");
					String desc = textFieldDesc.getText();
					int udsVenta=Integer.parseInt(textFieldVenta.getText());
					int udsAlmacen=Integer.parseInt(textFieldAlmacen.getText());
					int total=udsVenta+udsAlmacen;
					LocalDate fechaCad=LocalDate.parse(textFieldCaducidad.getText());
					insertProd.setString(1, desc);
					insertProd.setInt(2, udsVenta); 
					insertProd.setInt(3, udsAlmacen);
					insertProd.setInt(4, total);
					insertProd.setObject(5, fechaCad);
					insertProd.executeUpdate();
					JOptionPane.showMessageDialog(frmSupermercado, "Se ha añadido el producto");
					Statement muestra = con.createStatement();
					ResultSet prod = muestra.executeQuery("SELECT * FROM productos");
					productos.setRowCount(0);
					while (prod.next()) {
						Object[] row = new Object [6];
						row[0] = prod.getInt("codigo");
						row[1] = prod.getString("descripcion");
						row[2]=prod.getInt("unidades_venta");
						row[3]=prod.getInt("unidades_almacen");
						row[4]=prod.getInt("unidades_total");
						row[5]=prod.getObject("fecha_caducidad",LocalDate.class);
						productos.addRow(row);
						insertProd.close();
					}
				}catch (SQLException ex) {
					JOptionPane.showMessageDialog(frmSupermercado, ex.getMessage(),"Advertencia",JOptionPane.ERROR_MESSAGE);
				}
			}
			}
		});
		btnAgregar.setBounds(148, 455, 117, 25);
		frmSupermercado.getContentPane().add(btnAgregar);
		
		JButton btnActualizar = new JButton("ACTUALIZAR");
		btnActualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int codp=tablaProductos.getSelectedRow();
				TableModel model= tablaProductos.getModel();
				
				if (textFieldDesc.getText().length()==0) {
					JOptionPane.showMessageDialog(frmSupermercado, "La descipción está vacía","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (textFieldVenta.getText().length()==0){
					JOptionPane.showMessageDialog(frmSupermercado, "Las unidades en venta están vacías","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (textFieldAlmacen.getText().length()==0){
					JOptionPane.showMessageDialog(frmSupermercado, "Las unidades en almacén están vacías","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (textFieldCaducidad.getText().length()==0){
					JOptionPane.showMessageDialog(frmSupermercado, "La fecha de caducidad está vacía","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (!comprobarExpReg(textFieldDesc.getText(),"^\\D+$")){
					JOptionPane.showMessageDialog(frmSupermercado, "El nombre debe ser un conjunto de cadenas alfabéticas","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (!comprobarExpReg(textFieldVenta.getText(),"^\\d{1,2}$")){
					JOptionPane.showMessageDialog(frmSupermercado, "Las unidades de venta deben ser un número entero de dos cifras como mucho","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (!comprobarExpReg(textFieldAlmacen.getText(),"^\\d{1,2}$")){
					JOptionPane.showMessageDialog(frmSupermercado, "Las unidades de almacén deben ser un número entero de dos cifras como mucho","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else if (!comprobarExpReg(textFieldCaducidad.getText(),"^\\d{4}\\-\\d{2}\\-\\d{2}$")){
					JOptionPane.showMessageDialog(frmSupermercado, "La fecha de caducidad debe tener formato aaaa-mm-dd","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else  if ((LocalDate.parse(textFieldCaducidad.getText()).isBefore(LocalDate.now()))){
					JOptionPane.showMessageDialog(frmSupermercado, "La fecha de caducidad debe ser posterior a la fecha actual","Advertencia",JOptionPane.ERROR_MESSAGE);
				}else {
				try {					
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement actProd= con.prepareStatement("UPDATE productos SET descripcion =?, unidades_venta=?, unidades_almacen=? , unidades_total=? , fecha_caducidad=? WHERE codigo=?");
					String desc = textFieldDesc.getText();
					int udsVenta=Integer.parseInt(textFieldVenta.getText());
					int udsAlmacen=Integer.parseInt(textFieldAlmacen.getText());
					int total=udsVenta+udsAlmacen;
					LocalDate fechaCad=LocalDate.parse(textFieldCaducidad.getText());
					actProd.setString(1, desc);
					actProd.setInt(2, udsVenta); 
					actProd.setInt(3, udsAlmacen);
					actProd.setInt(4, total);
					actProd.setObject(5, fechaCad);
					actProd.setInt(6, (int) model.getValueAt(codp, 0));
					actProd.executeUpdate();
					JOptionPane.showMessageDialog(frmSupermercado, "Se ha actualizado el producto");
					Statement muestra = con.createStatement();
					ResultSet prod = muestra.executeQuery("SELECT * FROM productos");
					productos.setRowCount(0);
					while (prod.next()) {
						Object[] row = new Object [6];
						row[0] = prod.getInt("codigo");
						row[1] = prod.getString("descripcion");
						row[2]=prod.getInt("unidades_venta");
						row[3]=prod.getInt("unidades_almacen");
						row[4]=prod.getInt("unidades_total");
						row[5]=prod.getObject("fecha_caducidad",LocalDate.class);
						productos.addRow(row);	
					}
					actProd.close();
				}catch (SQLException ex) {
					JOptionPane.showMessageDialog(frmSupermercado, ex.getMessage(),"Advertencia",JOptionPane.ERROR_MESSAGE);
				}
			}		
			}
		});
		btnActualizar.setBounds(413, 455, 130, 25);
		frmSupermercado.getContentPane().add(btnActualizar);
		
		JButton btnBorrar = new JButton("BORRAR");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int codp=tablaProductos.getSelectedRow();
				TableModel model= tablaProductos.getModel();
				
				try {
					Connection con = ConnectionSingleton.getConnection();
					PreparedStatement delProd = con.prepareStatement("DELETE FROM productos WHERE codigo=?");
					delProd.setInt(1, (int) model.getValueAt(codp, 0));
					delProd.executeUpdate();
					JOptionPane.showMessageDialog(frmSupermercado, "Se ha borrado el producto");
					Statement muestra = con.createStatement();
					ResultSet prod = muestra.executeQuery("SELECT * FROM productos");
					productos.setRowCount(0);
					while (prod.next()) {
						Object[] row = new Object [6];
						row[0] = prod.getInt("codigo");
						row[1] = prod.getString("descripcion");
						row[2]=prod.getInt("unidades_venta");
						row[3]=prod.getInt("unidades_almacen");
						row[4]=prod.getInt("unidades_total");
						row[5]=prod.getObject("fecha_caducidad",LocalDate.class);
						productos.addRow(row);
					}
					delProd.close();
				}catch (SQLException ex) {
					JOptionPane.showMessageDialog(frmSupermercado, ex.getMessage(),"Advertencia",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnBorrar.setBounds(691, 455, 117, 25);
		frmSupermercado.getContentPane().add(btnBorrar);
		

		}catch (SQLException e) {
			JOptionPane.showMessageDialog(frmSupermercado, e.getMessage(),"Advertencia",JOptionPane.ERROR_MESSAGE);
		}
	}
}
